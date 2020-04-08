package ru.ifmo.rain.kuznetsov.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * Implementation class for {@link info.kgeorgiy.java.advanced.implementor.JarImpler} interface.
 *
 * @author Vladislav Kuznetsov
 */
public class JarImplementor extends Implementor implements JarImpler {

    /** Create new object
     *
     */
    public JarImplementor() {}

    /**
     * @throws ImplerException if we can't generate class:
     *                         1) Arguments (or one) are null
     *                         2) Class is primitive or array or final class
     *                         3) Class isn't an interface and contain only private constructors
     *                         4) Problem with writer.
     */
    @Override
    public void implementJar(Class<?> aClass, Path path) throws ImplerException {
        if (aClass == null || path == null) {
            throw new ImplerException("Token or class is null");
        }
        createDirectories(path);
        Path tempDir;
        String classPath;
        try {
            classPath = Path.of(aClass.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (URISyntaxException e) {
            throw new ImplerException("Failed to convert URL to URI", e);
        }
        try {
            tempDir = Files.createTempDirectory(path.toAbsolutePath().getParent(), "temp");
        } catch (IOException e) {
            throw new ImplerException("Can't create template directory", e);
        }
        try {
            implement(aClass, tempDir);
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            String[] args = new String[]{
                    "-classpath",
                    classPath,
                    tempDir.resolve(aClass.getPackageName().replace('.', File.separatorChar))
                            .resolve(generateClassName(aClass) + ".java").toString()
            };
            if (compiler.run(null, null, null, args) != 0) {
                throw new ImplerException("Can't compile files");
            }
            Manifest manifest = new Manifest();
            Attributes attributes = manifest.getMainAttributes();
            attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            ZipEntry zipEntry;
            String localPath = aClass.getPackageName().replace('.', '/') + "/" + generateClassName(aClass) + ".class";
            try (JarOutputStream writerJar = new JarOutputStream(Files.newOutputStream(path), manifest)) {
                zipEntry = new ZipEntry(localPath);
                writerJar.putNextEntry(zipEntry);
                Files.copy(getPath(tempDir, aClass, "class"), writerJar);
            } catch (IOException e) {
                throw new ImplerException("Can't write to JAR", e);
            }
        } finally {
            try {
                Files.walkFileTree(tempDir, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path file, IOException e) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new ImplerException("can't delete directory", e);
            }
        }
    }

    /**
     * Special function for debug. Save .jar file to "/home/vladkuznetsov/Vl/Projects/Java/java-advanced-2020/05. JarImplementor/HW05/test/".
     *
     * @param args args[0] - name of generate class.
     */
    public static void main(String[] args) {
        JarImplementor implementor = new JarImplementor();
        if (args.length < 2 || args[0] == null) {
            System.out.println("Wrong args");
            System.exit(0);
        }
        try {
            implementor.implementJar(Class.forName(args[0]), Path.of(args[1]));
        } catch (ImplerException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
