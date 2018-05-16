package com.protocol;

import com.kodgames.message.generaor.Main;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import org.dom4j.DocumentException;

/**
 * Created by jiangzhen
 */
public class CopyProtocolFileToClientDirectory {
	private static final String PROTOCOL_FILE_PATH = "E:\\Users\\Administrator\\Desktop\\server_http\\message\\protocode\\ProtocolCode_Platform.lua";
	private static final String PROTO_GENERATOR_BAT_DIR = "E:\\Users\\Administrator\\Desktop\\server_http\\message\\proto";
	private static final String PROTO_GENERATOR_BAT_NAME = "compile_proto_files.bat";

	private static final String CLIENT_COMMON_DIR_PATH = "E:\\Users\\Administrator\\Desktop\\Common";
	private static final String COMMIT_BAT_PATH = "C:\\Users\\kod\\Desktop\\MyJavaCode\\javacode\\KodGameTool\\svn_commit.bat";

	public static void main(String[] args) throws IOException, InterruptedException, DocumentException {
		// exec proto generator bat
		Process exec = Runtime.getRuntime().exec("cmd /c start "+ Paths.get(PROTO_GENERATOR_BAT_DIR, PROTO_GENERATOR_BAT_NAME).toAbsolutePath(), null, new File(PROTO_GENERATOR_BAT_DIR));
		exec.waitFor();

		// exec protocolId generator
		Main.main(null);

		// copy proto files
		Files.walkFileTree(Paths.get(PROTO_GENERATOR_BAT_DIR, "protobuf"), new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException
			{
				Files.copy(file, Paths.get(CLIENT_COMMON_DIR_PATH, "platform_jar","proto", file.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});

		// copy protocol files
		Files.walkFileTree(Paths.get(PROTO_GENERATOR_BAT_DIR, "pb"), new SimpleFileVisitor<Path>(){
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.copy(file, Paths.get(CLIENT_COMMON_DIR_PATH, "Protocols", "pb", file.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});

		// copy protocol desc file
		Files.copy(Paths.get(PROTOCOL_FILE_PATH), Paths.get(CLIENT_COMMON_DIR_PATH, "platform_jar", "protocode", "ProtocolCode_Platform.lua"), StandardCopyOption.REPLACE_EXISTING);

		// commit all to svn
		Files.copy(Paths.get(COMMIT_BAT_PATH), Paths.get(CLIENT_COMMON_DIR_PATH, "commit.bat"), StandardCopyOption.REPLACE_EXISTING);
		exec = Runtime.getRuntime().exec("cmd /c start "+ Paths.get(CLIENT_COMMON_DIR_PATH, "commit.bat"), null, new File(CLIENT_COMMON_DIR_PATH));
		exec.waitFor();
	}
}
