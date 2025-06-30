package liuyuyang.net.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.Map;

@Data
@TableName(value = "env_config", autoResultMap = true)
public class EnvConfig {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> value;
}
