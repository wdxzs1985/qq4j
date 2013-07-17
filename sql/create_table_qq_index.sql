delimiter $$

CREATE TABLE `qq_index` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `word` varchar(20) NOT NULL,
  `upd_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flg` int(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

