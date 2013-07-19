delimiter $$

CREATE TABLE `qq_messages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `qq` int(15) unsigned NOT NULL,
  `owner` int(15) unsigned NOT NULL,
  `message` varchar(200) NOT NULL,
  `answer` varchar(200) NOT NULL,
  `public_flg` int(1) unsigned NOT NULL DEFAULT '0',
  `upd_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flg` int(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8$$

