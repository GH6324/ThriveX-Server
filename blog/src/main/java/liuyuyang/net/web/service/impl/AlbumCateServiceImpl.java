package liuyuyang.net.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import liuyuyang.net.common.execption.CustomException;
import liuyuyang.net.model.AlbumCate;
import liuyuyang.net.web.mapper.AlbumCateMapper;
import liuyuyang.net.web.service.AlbumCateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class AlbumCateServiceImpl extends ServiceImpl<AlbumCateMapper, AlbumCate> implements AlbumCateService {
    @Resource
    private AlbumCateMapper albumCateMapper;

    @Override
    public void add(AlbumCate albumCate) {
        albumCateMapper.insert(albumCate);
    }

    @Override
    public void del(Integer id) {
        AlbumCate albumCate = albumCateMapper.selectById(id);
        if (albumCate == null) throw new CustomException(400, "该相册不存在");
        albumCateMapper.deleteById(id);
    }

    @Override
    public void batchDel(List<Integer> ids) {
        albumCateMapper.deleteBatchIds(ids);
    }

    @Override
    public void edit(AlbumCate albumCate) {
        AlbumCate existAlbumCate = albumCateMapper.selectById(albumCate.getId());
        if (existAlbumCate == null) throw new CustomException(400, "该相册不存在");
        updateById(albumCate);
    }

    @Override
    public AlbumCate get(Integer id) {
        AlbumCate albumCate = albumCateMapper.selectById(id);
        if (albumCate == null) throw new CustomException(400, "该相册不存在");
        return albumCate;
    }

    @Override
    public List<AlbumCate> list() {
        LambdaQueryWrapper<AlbumCate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(AlbumCate::getId);
        return albumCateMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public Page<AlbumCate> paging(Integer page, Integer size) {
        LambdaQueryWrapper<AlbumCate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(AlbumCate::getId);
        return page(new Page<>(page, size), lambdaQueryWrapper);
    }
}