delimiter $$

CREATE TABLE `qq_user` (
  `account` int(10) unsigned NOT NULL,
  `qq` int(10) unsigned NOT NULL,
  `nickname` varchar(45) NOT NULL,
  `faith` int(10) unsigned NOT NULL DEFAULT '0',
  `upd_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flg` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`account`,`qq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

