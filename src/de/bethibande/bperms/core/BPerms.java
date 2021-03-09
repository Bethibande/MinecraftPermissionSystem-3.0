package de.bethibande.bperms.core;

import de.bethibande.bperms.configs.GeneralConfig;
import de.bethibande.bperms.configs.MysqlConfig;
import de.bethibande.bperms.management.GroupManager;
import de.bethibande.bperms.management.UserManager;
import de.bethibande.bperms.struct.PermissionGroup;
import de.bethibande.bperms.utils.ConfigUtils;
import de.bethibande.bperms.utils.FileUtils;
import de.bethibande.bperms.utils.ILogger;
import de.bethibande.bperms.utils.Sql;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BPerms {

    public static final String INVENTORY_HEADER = "§b§lBPerms";
    public static final String CONSOLE_PREFIX = "§bBPerms §7» ";
    public static final String CHAT_PREFIX = "§b§lBPerms §8» §7";

    @Getter
    private static BPerms instance;

    @Getter
    private final String name;
    @Getter
    private final String version;
    @Getter
    private final String description;
    @Getter
    private final File configDirectory;

    @Getter
    @Setter
    private ILogger logger;

    @Getter
    private MysqlConfig mysqlConfig;
    @Getter
    private Sql mysql;
    @Getter
    public String mysql_table_prefix = "bperms_";

    @Getter
    private GeneralConfig config;

    @Getter
    private GroupManager groupManager;
    @Getter
    private UserManager userManager;

    public BPerms(String _name, String _version, String _description, File _configDirectory) {
        name = _name;
        version = _version;
        description = _description;
        configDirectory = _configDirectory;
        instance = this;
    }

    public void enable() {
        logger.log("§aEnabling plugin..");
        logger.log("§a" + getName() + " " + getVersion() + " §amade by Bethibande.");

        loadConfigs();

        mysql = new Sql(mysqlConfig.host, mysqlConfig.port, mysqlConfig.username, mysqlConfig.password, mysqlConfig.database);
        mysql.connect();

        loadGeneralConfigFromSql();
        initGroupManager();
        initUserManager();

        if(!groupManager.groupExists(config.default_parent_group)) {
            PermissionGroup defaultGroup = groupManager.createGroup(config.default_parent_group, "default");
            defaultGroup.setDefault(true);
            defaultGroup.setPrefix("§7Player");
            groupManager.saveChanges(defaultGroup.getId());
        }

        logger.log("§aPlugin enabled!");
    }

    public void saveConfigToMysql() {
        mysql.update("update `" + mysql_table_prefix + "config` set `value`='" + config.maxObjectCacheLifetime + "' where `key`='max_object_cache_lifetime';");
        mysql.update("update `" + mysql_table_prefix + "config` set `value`='" + config.default_parent_group + "' where `key`='default_parent_group';");
        mysql.update("update `" + mysql_table_prefix + "config` set `value`='" + config.chat_format + "' where `key`='chat_format';");
    }

    private void loadConfigs() {
        if(!configDirectory.exists()) {
            if(!configDirectory.mkdirs()) {
                logger.logError("An error occurred while creating the data folder!");
                disable();
            }
        }

        File sqlConfigFile = new File(configDirectory + "/mysql_config.json");
        if(!sqlConfigFile.exists()) {
            if(!FileUtils.createFile(sqlConfigFile)) {
                logger.logError("An error occurred while loading the config files: couldn't create the mysql config file!");
                disable();
            }
            mysqlConfig = new MysqlConfig();
            ConfigUtils.saveConfig(mysqlConfig, sqlConfigFile);
        } else mysqlConfig = (MysqlConfig) ConfigUtils.loadConfig(MysqlConfig.class, sqlConfigFile);

        logger.log("§aIf you have changed any config settings, please reload or restart the server");
    }

    private void initUserManager() {
        userManager = new UserManager(UserManager.initSqlUserStorage("userStorage", mysql_table_prefix + "userStorage", config.maxObjectCacheLifetime));
    }

    private void loadGeneralConfigFromSql() {
        config = new GeneralConfig();
        try {
            mysql.update("create table if not exists `" + mysql_table_prefix + "config`(`key` VARCHAR(128), `value` TEXT(65000), UNIQUE KEY(`key`));");

            ResultSet rs = mysql.query("select * from `" + mysql_table_prefix + "config` where `key`='chat_format';");
            if(rs.next()) {
                config.chat_format = rs.getString("value");
            } else mysql.update("insert into `" + mysql_table_prefix + "config` values ('chat_format', '" + config.chat_format + "');");

            rs = mysql.query("select * from `" + mysql_table_prefix + "config` where `key`='max_object_cache_lifetime';");
            if(rs.next()) {
                config.maxObjectCacheLifetime = Integer.parseInt(rs.getString("value"));
            } else mysql.update("insert into `" + mysql_table_prefix + "config` values ('max_object_cache_lifetime', '" + config.maxObjectCacheLifetime + "');");

            rs = mysql.query("select * from `" + mysql_table_prefix + "config` where `key`='default_parent_group';");
            if(rs.next()) {
                config.default_parent_group = UUID.fromString(rs.getString("value"));
            } else mysql.update("insert into `" + mysql_table_prefix + "config` values ('default_parent_group', '" + config.default_parent_group + "');");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void initGroupManager() {
        groupManager = new GroupManager();
        groupManager.initGroupStorage("groupStorage", mysql_table_prefix + "groupStorage");
        groupManager.loadAllGroupsFromMysql();
    }

    public void disable() {
        logger.log("Disabling the plugin..");

        saveConfigs();

        logger.log("Plugin successfully disabled!");
    }

    private void saveConfigs() {
        File sqlConfigFile = new File(configDirectory + "/mysql_config.json");
        if(!sqlConfigFile.exists()) FileUtils.createFile(sqlConfigFile);
        ConfigUtils.saveConfig(mysqlConfig, sqlConfigFile);

        logger.log("Configs save");
    }



}
