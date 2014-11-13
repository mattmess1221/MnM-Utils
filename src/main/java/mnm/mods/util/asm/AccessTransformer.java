package mnm.mods.util.asm;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Access Transformers for use in LiteMods
 *
 * @author Matthew Messinger
 *
 */
public class AccessTransformer implements IClassTransformer {

    private Map<String, List<Access>> fields = Maps.newHashMap();
    private Map<String, List<Access>> methods = Maps.newHashMap();

    public AccessTransformer() {
        findAccessTransformers();
    }

    private void findAccessTransformers() {
        final String AT_MANIFEST = "FMLAT";

        URLClassLoader cl = (URLClassLoader) getClass().getClassLoader();
        for (URL url : cl.getURLs()) {
            JarFile jar = null;
            try {
                List<String> lines = Lists.newArrayList();
                try {
                    jar = new JarFile(new File(url.toURI()));
                    Manifest manifest = jar.getManifest();
                    if (manifest == null)
                        continue;
                    String fmlat = manifest.getMainAttributes().getValue(AT_MANIFEST);

                    if (fmlat != null) {
                        JarEntry entry = jar.getJarEntry("META-INF/" + fmlat);
                        lines = IOUtils.readLines(jar.getInputStream(entry));
                    }
                } catch (FileNotFoundException e) {
                    // It's a directory, ignore.
                }
                parseLines(lines.toArray(new String[0]));

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(jar);
            }
        }
    }

    private void parseLines(String[] lines) {
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("#"))
                continue;
            if (line.isEmpty())
                continue;

            int stop = line.indexOf('#');
            line = line.substring(0, stop - 1);
            String[] split = line.split(" ");
            String access = split[0];
            String className = split[1];
            String item = split[2];
            if (item.endsWith(")")) {
                // It's a method
                int paren = item.indexOf('(');
                String name = item.substring(0, paren - 1);
                String desc = item.substring(paren);
                Access ma = new Access(access, className, name, desc);
                getListFromMap(ma.className, methods).add(ma);
            } else {
                // It's a field
                Access fa = new Access(access, className, item);
                getListFromMap(fa.className, fields).add(fa);
            }
        }
    }

    private <T> List<T> getListFromMap(String key, Map<String, List<T>> map) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<T>());
        }
        return map.get(key);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {

        if (bytes == null) {
            return null;
        }

        if (!methods.containsKey(transformedName) && !fields.containsKey(transformedName)) {
            return bytes;
        }

        // Read class
        ClassNode cn = new ClassNode();
        ClassReader cr = new ClassReader(bytes);
        cr.accept(cn, 0);

        // Make the changes
        for (MethodNode method : cn.methods) {
            transformMethod(transformedName, method);
        }
        for (FieldNode field : cn.fields) {
            transformField(transformedName, field);
        }

        // Write the class
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }

    private void transformMethod(String className, MethodNode method) {
        for (Access access : getListFromMap(className, methods)) {
            if ((access.name.equals(method.name) && access.desc.equals(method.desc))
                    || access.name.equals("*")) {
                method.access = getAccess(method.access, access);

            }
        }
    }

    private void transformField(String className, FieldNode field) {
        for (Access access : getListFromMap(className, fields)) {
            if (access.name.equals(field.name) || access.name.equals("*")) {
                field.access = getAccess(field.access, access);
            }
        }
    }

    // Method from FML
    int getAccess(int access, Access target) {
        final int PRIVATE = Opcodes.ACC_PRIVATE;
        final int DEFAULT = 0;
        final int PROTECTED = Opcodes.ACC_PROTECTED;
        final int PUBLIC = Opcodes.ACC_PUBLIC;

        int t = target.targetAccess;
        int ret = (access & ~7);

        switch (access & 7) {
        case PRIVATE:
            ret |= t;
            break;
        case DEFAULT:
            ret |= (t != PRIVATE ? t : DEFAULT);
            break;
        case PROTECTED:
            ret |= (t != PRIVATE && t != DEFAULT ? t : PROTECTED);
            break;
        case PUBLIC:
            ret |= (t != PRIVATE && t != DEFAULT & t != PROTECTED ? t : PUBLIC);
            break;
        default:
            throw new RuntimeException("That shouldn't have happened.  Report this.");
        }

        if (target.changeFinal) {
            if (target.markFinal) {
                ret |= Opcodes.ACC_FINAL;
            } else {
                ret &= ~Opcodes.ACC_FINAL;
            }
        }

        return ret;
    }

    class Access {

        int targetAccess;
        int access;
        String className;
        String name;
        String desc;
        boolean changeFinal;
        boolean markFinal;

        Access(String access, String className, String name, String desc) {
            this(access, className, name);
            this.desc = desc;
        }

        Access(String access, String className, String name) {
            this.className = className;
            this.name = name;

            if (access.startsWith("public")) {
                targetAccess = ACC_PUBLIC;
            } else if (access.startsWith("private")) {
                targetAccess = ACC_PRIVATE;
            } else if (access.startsWith("protected")) {
                targetAccess = ACC_PROTECTED;
            }

            if (access.endsWith("-f")) {
                changeFinal = true;
                markFinal = false;

            } else if (access.endsWith("+f")) {
                changeFinal = true;
                markFinal = true;
            }
        }

        @Override
        public String toString() {
            String access = "default";
            switch (targetAccess) {
            case ACC_PUBLIC:
                access = "public";
                break;
            case ACC_PROTECTED:
                access = "protected";
                break;
            case ACC_PRIVATE:
                access = "private";
                break;
            }

            String f = "";
            if (changeFinal) {
                f = markFinal ? "+f" : "-f";
            }

            return String.format("%s%s %s %s%s", access, f, className, name, desc);
        }
    }

}
