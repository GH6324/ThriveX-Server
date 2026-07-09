-- 里程碑表结构迁移（已有数据库执行）
-- 若某列已删除，对应语句会报错，跳过即可

ALTER TABLE `milestone` DROP COLUMN `sort_order`;
ALTER TABLE `milestone` DROP COLUMN `views`;
ALTER TABLE `milestone` DROP COLUMN `likes`;
