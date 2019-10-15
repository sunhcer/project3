package com.stylefeng.guns.rest.common.persistence.cinema;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.cinema.model.*;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeAreaDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeBrandDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeBrandDictT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {
    @Autowired
    private MtimeBrandDictTMapper mtimeBrandDictTMapper;
    @Autowired
    private MtimeAreaDictTMapper mtimeAreaDictTMapper;
    @Autowired
    private MtimeHallDictTMapper mtimeHallDictTMapper;
    @Autowired
    private MtimeCinemaTMapper mtimeCinemaTMapper;
    @Override
    public List<BrandVo> queryBrands(Integer brandId) {
        List<BrandVo> brandVos = mtimeBrandDictTMapper.getBrands();
        return brandVos;

    }

    @Override
    public List<AreaVo> queryAreas(Integer areaId) {
        List<AreaVo> area = mtimeAreaDictTMapper.getArea();
        return area;
    }

    @Override
    public List<HallTypeVo> queryHallTypes(Integer hallType) {
        List<HallTypeVo> hallType1 = mtimeHallDictTMapper.getHallType();
        return hallType1;
    }

    @Override
    public List<CinemaVo> getCinemas(CinemaQueryVo cinemaQueryVo) {
        List<CinemaVo> cinemas = mtimeCinemaTMapper.getCinemas();
        return cinemas;
    }


}
