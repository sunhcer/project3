package com.stylefeng.guns.rest.modular.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.cinema.model.*;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;

    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;
    @Autowired
    private MtimeBrandDictTMapper mtimeBrandDictTMapper;
    @Autowired
    private MtimeAreaDictTMapper mtimeAreaDictTMapper;
    @Autowired
    private MtimeHallDictTMapper mtimeHallDictTMapper;
    @Autowired
    private MoocOrderTMapper moocOrderTMapper;

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
//        List<CinemaVo> cinemas = mtimeCinemaTMapper.getCinemas();
        List<CinemaVo> cinemas = new ArrayList<>();
        ///cinema/getCinemas?brandId=99&areaId=7&halltypeId=1&pageSize=12&nowPage=1
        Integer nowPage = cinemaQueryVo.getNowPage();
        Integer pageSize = cinemaQueryVo.getPageSize();
        //偏移量
        int offset = (nowPage - 1) * pageSize;
        //
        RowBounds rowBounds = new RowBounds(offset, pageSize);
        //条件生成  判断
        EntityWrapper<MtimeCinemaT> wrapper = new EntityWrapper<>();
        //
        Integer brandId = cinemaQueryVo.getBrandId();
        Integer areaId = cinemaQueryVo.getAreaId();
        Integer hallType = cinemaQueryVo.getHallType();

        if (brandId != 99){
            wrapper.eq("brand_id", brandId);
        }
        if (areaId != 99){
            wrapper.eq("area_id", areaId);
        }
        if (hallType != 99){
            wrapper.like("hall_ids", hallType+"");
        }
        //分页
        List<MtimeCinemaT> mtimeCinemaTS = mtimeCinemaTMapper.selectPage(rowBounds, wrapper);

        for (MtimeCinemaT mtimeCinemaT : mtimeCinemaTS) {
            CinemaVo cinemaVo = new CinemaVo();
            //返回信息
            cinemaVo.setCinemaAddress(mtimeCinemaT.getCinemaAddress());
            cinemaVo.setCinemaName(mtimeCinemaT.getCinemaName());
            cinemaVo.setUuid(mtimeCinemaT.getUuid().toString());
            cinemaVo.setMinimumPrice(mtimeCinemaT.getMinimumPrice().toString());
            //
            cinemas.add(cinemaVo);
        }
        //计算totalPage
        Integer integer = mtimeCinemaTMapper.selectCount(wrapper);
        BigDecimal count = new BigDecimal(integer);
        BigDecimal pageSizeBig = new BigDecimal(pageSize);
        BigDecimal divide = count.divide(pageSizeBig, 0, RoundingMode.FLOOR);
        int totalPage = divide.intValue();
        //
        cinemaQueryVo.setTotalPage(totalPage);

        return cinemas;
    }

    @Override
    public CinemaInfo getCinemaInfoByCinemaId(Integer cinemaId) {
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);

        CinemaInfo cinemaInfo = CinemaInfo.builder()
                .cinemaAdress(mtimeCinemaT.getCinemaAddress())
                .cinemaId(mtimeCinemaT.getUuid())
                .cinemaName(mtimeCinemaT.getCinemaName())
                .cinemaPhone(mtimeCinemaT.getCinemaPhone())
                .imgUrl(mtimeCinemaT.getImgAddress())
                .build();

        return cinemaInfo;
    }

    @Override
    public List<Film> getFilmListByCinemaId(Integer cinemaId) {
        EntityWrapper<MtimeFieldT> wrapper = new EntityWrapper<>();
        wrapper.eq("cinema_id",cinemaId);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(wrapper);
        ArrayList<MtimeHallFilmInfoT> mtimeHallFilmInfoTList = new ArrayList<>();
        ArrayList<Integer> integers = new ArrayList<>();

        for (MtimeFieldT mtimeFieldT : mtimeFieldTS) {
            Integer filmId = mtimeFieldT.getFilmId();
            if (!integers.contains(filmId)) {
                integers.add(filmId);
            }
        }
        for (Integer filmId : integers) {
            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectByFilmId(filmId);

            mtimeHallFilmInfoTList.add(mtimeHallFilmInfoT);
        }

        ArrayList<Film> films = new ArrayList<>();
        for (MtimeHallFilmInfoT mtimeHallFilmInfoT : mtimeHallFilmInfoTList) {

            Film film = Film.builder()
                    .filmType(mtimeHallFilmInfoT.getFilmLanguage())
                    .imgAddress(mtimeHallFilmInfoT.getImgAddress())
                    .filmName(mtimeHallFilmInfoT.getFilmName())
                    .filmLength(mtimeHallFilmInfoT.getFilmLength())
                    .filmId(mtimeHallFilmInfoT.getFilmId())
                    .filmFields(getFilmFieldsByCinemaIdAndFilmId(cinemaId, mtimeHallFilmInfoT.getFilmId()))
                    .filmCats(mtimeHallFilmInfoT.getFilmCats())
                    .actors(mtimeHallFilmInfoT.getActors())
                    .build();
            films.add(film);
        }
        return films;
    }

    @Override
    public List<FilmField> getFilmFieldsByCinemaIdAndFilmId(Integer cinemaId,Integer filmId) {
        EntityWrapper<MtimeFieldT> wrapper = new EntityWrapper<>();
        wrapper.eq("cinema_id", cinemaId);
        wrapper.eq("film_id", filmId);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(wrapper);
        ArrayList<FilmField> filmFields = new ArrayList<>();
        for (MtimeFieldT mtimeFieldT : mtimeFieldTS) {
            FilmField filmField = new FilmField();
            filmField.setBeginTime(mtimeFieldT.getBeginTime());
            filmField.setEndTime(mtimeFieldT.getEndTime());
            filmField.setFieldId(mtimeFieldT.getUuid());
            filmField.setHallName(mtimeFieldT.getHallName());
            filmField.setPrice(mtimeFieldT.getPrice().toString());

            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectByFilmId(mtimeFieldT.getFilmId());

            filmField.setLanguage(mtimeHallFilmInfoT.getFilmLanguage());
            filmFields.add(filmField);
        }
        return filmFields;
    }

    @Override
    public HallInfo getHallInfoByFieldId(Integer fieldId) {
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(mtimeFieldT.getHallId());
        String seatAddress = mtimeHallDictT.getSeatAddress();

        HallInfo hallInfo = HallInfo.builder()
                .discountPrice(null)
                .hallFieldId(mtimeFieldT.getHallId())
                .hallName(mtimeFieldT.getHallName())
                .price(mtimeFieldT.getPrice().toString())
                .seatFile(seatAddress)
                .soldSeats(getSoldSeatsByFieldId(fieldId))
                .build();

        return hallInfo;
    }

    @Override
    public Film getFilmInfoByFieldId(Integer fieldId) {

        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        Integer filmId = mtimeFieldT.getFilmId();

        MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectByFilmId(filmId);
        Film film = Film.builder()
                .actors(null)
                .filmCats(mtimeHallFilmInfoT.getFilmCats())
                .filmFields(null)
                .filmId(mtimeHallFilmInfoT.getFilmId())
                .filmLength(mtimeHallFilmInfoT.getFilmLength())
                .filmName(mtimeHallFilmInfoT.getFilmName())
                .imgAddress(mtimeHallFilmInfoT.getImgAddress())
                .filmType(mtimeHallFilmInfoT.getFilmLanguage())
                .build();

        return film;
    }


    //根据fieldId查已售出座位
    private String getSoldSeatsByFieldId(Integer fieldId) {
        EntityWrapper<MoocOrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("field_id",fieldId);
        wrapper.in("order_status", new Integer[]{0,1});
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectList(wrapper);
        StringBuilder soldSeats = new StringBuilder();
        for (MoocOrderT moocOrderT : moocOrderTS) {
            String seatsIds = moocOrderT.getSeatsIds();
            soldSeats.append(seatsIds).append(",");
        }
        if(soldSeats.length() != 0) {
            soldSeats.deleteCharAt(soldSeats.length() - 1);
        }

        return soldSeats.toString();
    }
}

