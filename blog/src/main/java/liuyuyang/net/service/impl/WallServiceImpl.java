package liuyuyang.net.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import liuyuyang.net.execption.CustomException;
import liuyuyang.net.mapper.WallCateMapper;
import liuyuyang.net.mapper.WallMapper;
import liuyuyang.net.model.Wall;
import liuyuyang.net.model.WallCate;
import liuyuyang.net.service.WallService;
import liuyuyang.net.utils.EmailUtils;
import liuyuyang.net.utils.YuYangUtils;
import liuyuyang.net.vo.PageVo;
import liuyuyang.net.vo.wall.WallFilterVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class WallServiceImpl extends ServiceImpl<WallMapper, Wall> implements WallService {
    @Resource
    private YuYangUtils yuYangUtils;
    @Resource
    private WallMapper wallMapper;
    @Resource
    private WallCateMapper wallCateMapper;
    @Resource
    private EmailUtils emailUtils;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void add(Wall wall) throws Exception {
        wallMapper.insert(wall);
        emailUtils.send(from, "您有新的留言等待审核", "");
    }

    @Override
    public Wall get(Integer id) {
        Wall data = wallMapper.selectById(id);
        if (data == null) throw new CustomException(400, "该留言不存在");
        data.setCate(wallCateMapper.selectById(data.getCateId()));
        return data;
    }

    @Override
    public List<Wall> list(WallFilterVo filterVo) {
        QueryWrapper<Wall> queryWrapper = yuYangUtils.queryWrapperFilter(filterVo, "content");
        queryWrapper.eq("audit_status", filterVo.getStatus());

        if (filterVo.getCateId() != null) {
            queryWrapper.eq("cate_id", filterVo.getCateId());
        }

        List<Wall> list = wallMapper.selectList(queryWrapper);

        // 绑定数据
        for (Wall wall : list) {
            wall.setCate(wallCateMapper.selectById(wall.getCateId()));
        }

        return list;
    }

    @Override
    public Page<Wall> paging(WallFilterVo filterVo, PageVo pageVo) {
        List<Wall> list = list(filterVo);
        return yuYangUtils.getPageData(pageVo, list);
    }

    @Override
    public Page<Wall> getCateWallList(Integer cateId, PageVo pageVo) {
        QueryWrapper<Wall> queryWrapper = new QueryWrapper<>();
        if (cateId != 1) queryWrapper.eq("cate_id", cateId);
        queryWrapper.eq("audit_status", 1);
        queryWrapper.orderByDesc("create_time");

        Page<Wall> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        wallMapper.selectPage(page, queryWrapper);

        List<Wall> list = page.getRecords();

        // 绑定数据
        for (Wall wall : list) {
            wall.setCate(wallCateMapper.selectById(wall.getCateId()));
        }

        // 分页处理
        return page;
    }

    @Override
    public List<WallCate> getCateList() {
        QueryWrapper<WallCate> queryWrapper = new QueryWrapper<>();
        List<WallCate> list = wallCateMapper.selectList(queryWrapper);
        return list;
    }
}
