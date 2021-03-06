package com.Share.dao.impl;

import org.springframework.stereotype.Repository;

import com.Share.dao.ILikeDao;
import com.Share.entity.Like;
import com.Share.entity.ShareCondition;
import com.Share.util.Pagination;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Repository
public class LikeDao extends BaseDao<Like> implements ILikeDao<Like> {

    @Override
    public Pagination<Like> findLikeByPage(ShareCondition shareCondition) {
        StringBuffer hql = new StringBuffer();
        ArrayList<Object> params = new ArrayList<>();

        hql.append("from Like where 1=1 ");
        //条件：用户id
        if (shareCondition.getUserId() != null) {
            hql.append("and id.user.id=? ");
            params.add(shareCondition.getUserId());
        }
        //条件：公开分享，用户没有被屏蔽，或我的(隐私)分享
        if (shareCondition.getCurrentUserId() != null) {
            hql.append("and (id.share.status=0 and id.share.user.status=0 or id.share.user.id=?) ");
            params.add(shareCondition.getCurrentUserId());
        }

        //根据时间倒序排序
        hql.append("order by createTime desc");

        return findPagination(hql.toString(), params.toArray(), shareCondition.getPageNo(), shareCondition.getPageSize());
    }
}
