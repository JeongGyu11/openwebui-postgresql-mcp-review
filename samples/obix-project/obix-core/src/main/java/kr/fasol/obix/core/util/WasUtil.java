package kr.fasol.obix.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > WAS 관련 Util
 * 프로그램명 : WAS 관련 Util
 * 설명 : 
 *
 * @Class  WasUtil.class
 * @author  정영탁
 * @since   2017-08-01
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-08-01        정영탁    최초생성
 */
@Slf4j
@UtilityClass
public class WasUtil {

    /**
     * Jeus 서버의 Container명 반환
     */
    public static String getJeusContainerName() {
        String containerName = null;
        try {
            Class<?> jeusManagerPropertyValuesClass = Class.forName("jeus.util.properties.JeusManagerPropertyValues");
            Field containerNameField = jeusManagerPropertyValuesClass.getDeclaredField("containerName");
            containerNameField.setAccessible(true);
            Object obj = containerNameField.get(jeusManagerPropertyValuesClass);
            containerName = ((String) obj).substring(((String) obj).lastIndexOf(95) + 1);
        }
        catch (IllegalArgumentException e) {
        	log.error(e.getMessage());
        	containerName = EnvironmentUtil.getProperty("application.name");
        }
        catch (Exception e) {
        	log.error(e.getMessage());
        	containerName = EnvironmentUtil.getProperty("application.name");
        }
        return containerName;
    }

    /**
     * Tomcat 서버의 Container명 반환
     */
    public static String getTomcatContainerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "tomcat01";
        }
    }

    /**
     * WAS의 Container명 반환
     */
    public static String getContainerName() {
        String name = "main";

        WAS_KIND kind = getWasName();
        if (kind == WAS_KIND.JEUS) {
            return getJeusContainerName();
        } else if (kind == WAS_KIND.TOMCAT) {
            return getTomcatContainerName();
        } else {
            return name;
        }
    }

    /**
     * WAS 종류 반환
     */
    public static WAS_KIND getWasName() {
        if (WasUtil.class.getClassLoader() != null) {
            if (WasUtil.class.getClassLoader().getClass().getName().startsWith("jeus")) {
                return WAS_KIND.JEUS;
            }
            if (WasUtil.class.getClassLoader().getClass().getName().startsWith("com.ibm")) {
                return WAS_KIND.WEB_SPHERE;
            }
        }
        if (System.getProperty("catalina.home") != null) {
            return WAS_KIND.TOMCAT;
        }
        return WAS_KIND.UNDEFINDED;
    }

    public static enum WAS_KIND {
        JEUS, WEB_SPHERE, WEB_LOGIC, TOMCAT, UNDEFINDED;
    }

    /**
     * 특정 패키지의 class 목록 반환
     */
	public static List<Class<?>> getClassList(String packagePath) throws Exception {
		List<Class<?>> classList = new ArrayList<>();
		Package targetPackage = Package.getPackage(packagePath);
		File directory = null;
		if (targetPackage == null) {
			log.error("Inserted Package Path({}) is invalid!!!", packagePath);
			return null;
		}

		String relPath = packagePath.replace('.', '/');
		log.debug("ClassDiscovery: Package: {}, becomes Path:{}", packagePath, relPath);
		URL resource = WasUtil.class.getClassLoader().getResource(relPath);
		log.debug("ClassDiscovery: Resource = {}", resource);
		try {
			directory = new File(resource.toURI());
		} catch (URISyntaxException e) {
			log.error("Inserted Resource Path({}) is invalid!!!", relPath);
			directory = null;
		} catch (Exception e) {
			log.error("Inserted Resource Path({}) is invalid!!!", relPath);
			directory = null;
		}

		if (directory != null && directory.exists()) {
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(".class")) {
					String className = targetPackage.getName() + '.' + files[i].substring(0, files[i].length() - 6);
					log.debug("ClassDiscovery: className = {}", className);
					classList.add(Class.forName(className));
				}
			}
		} else {
			String fullPath = resource.getFile();
			log.debug("ClassDiscovery: fullPath = {}", fullPath);
			String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
			@SuppressWarnings("resource")
			JarFile jf = new JarFile(jarPath);
			for (Enumeration<JarEntry> entries = jf.entries() ; entries.hasMoreElements() ;) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();
				if (entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
					log.debug("ClassDiscovery: JarEntry = {}", entryName);
					String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
					log.debug("ClassDiscovery: className = {}", className);
					classList.add(Class.forName(className));
				}
			}
		}

		return classList;
	}

}
