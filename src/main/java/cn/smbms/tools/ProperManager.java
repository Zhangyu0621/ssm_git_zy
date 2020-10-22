package cn.smbms.tools;

import cn.smbms.dao.BaseDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProperManager {

    private static ProperManager properManager;

    Properties params;
   // private static Properties params;

    /**
     * 私有构造
     */
    private ProperManager() {
        params = new Properties();
        String configFile = "database.properties";
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream(configFile);
        try {
            params.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向外界提供一个公有方法
     * @return
     */
    public static ProperManager getInstance() {

        return ProperManager_Helper.properManager();
    }

    /**
     * 静态内部类(解决缓存)
     */
   public static class ProperManager_Helper {
        /**
         * 提供一个方法，创建对象
         *
         * @return
         */
        public static ProperManager properManager() {
            return new ProperManager();
        }
    }

    /**
     * 实例方法
     * @param key
     * @return
     */
    public String getValueByKey(String key) {
        return params.getProperty(key);
    }
}
