package com.example.yinggutest.temporaryentity;

import java.util.List;

/**
 * Created by Leo on 2020/10/18.
 */

//没有后台数据，所以暂时拿这个的数据顶一下


public class WXDataEntity {

    /**
     * data : {"curPage":0,"datas":[{"apkLink":"","audit":1,"author":"腾讯Bugly","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":1323,"link":"https://mp.weixin.qq.com/s/jhVzFa6DatRNK9anuDoSUA","niceDate":"2017-10-15 18:24","niceShareDate":"未知时间","origin":"","prefix":"","projectLink":"","publishTime":1508063069000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"Android 7.0中ContentProvider实现原理","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"CrazyCodeBoy","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":1135,"link":"http://www.jianshu.com/p/ad8c066e9166","niceDate":"2017-05-25 14:29","niceShareDate":"未知时间","origin":"简书","prefix":"","projectLink":"","publishTime":1495693753000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"INSTALL FAILED CONFLICTING PROVIDER问题完美解决方案","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"qq_26787115","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":868,"link":"http://blog.csdn.net/qq_26787115/article/details/50573432","niceDate":"2016-06-21 19:18","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1466507913000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"Android实训案例（五）\u2014\u2014四大组件之一ContentProvider的使用，通讯录的实现以及ListView的优化","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":49,"link":"http://blog.csdn.net/liuhe688/article/details/7006556","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之六：ContentProvider之读写联系人","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":50,"link":"http://blog.csdn.net/liuhe688/article/details/7020612","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之七：ContentProvider之读写短消息","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":51,"link":"http://blog.csdn.net/liuhe688/article/details/7050868","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之八：创建及调用自己的ContentProvider","type":0,"userId":-1,"visible":1,"zan":0}],"offset":-20,"over":false,"pageCount":1,"size":20,"total":6}
     * errorCode : 0
     * errorMsg :
     */

    private DataBean data;
    private int errorCode;
    private String errorMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static class DataBean {
        /**
         * curPage : 0
         * datas : [{"apkLink":"","audit":1,"author":"腾讯Bugly","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":1323,"link":"https://mp.weixin.qq.com/s/jhVzFa6DatRNK9anuDoSUA","niceDate":"2017-10-15 18:24","niceShareDate":"未知时间","origin":"","prefix":"","projectLink":"","publishTime":1508063069000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"Android 7.0中ContentProvider实现原理","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"CrazyCodeBoy","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":1135,"link":"http://www.jianshu.com/p/ad8c066e9166","niceDate":"2017-05-25 14:29","niceShareDate":"未知时间","origin":"简书","prefix":"","projectLink":"","publishTime":1495693753000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"INSTALL FAILED CONFLICTING PROVIDER问题完美解决方案","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"qq_26787115","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":868,"link":"http://blog.csdn.net/qq_26787115/article/details/50573432","niceDate":"2016-06-21 19:18","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1466507913000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"Android实训案例（五）\u2014\u2014四大组件之一ContentProvider的使用，通讯录的实现以及ListView的优化","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":49,"link":"http://blog.csdn.net/liuhe688/article/details/7006556","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之六：ContentProvider之读写联系人","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":50,"link":"http://blog.csdn.net/liuhe688/article/details/7020612","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之七：ContentProvider之读写短消息","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","audit":1,"author":"liuhe688","canEdit":false,"chapterId":17,"chapterName":"ContentProvider","collect":false,"courseId":13,"desc":"","descMd":"","envelopePic":"","fresh":false,"id":51,"link":"http://blog.csdn.net/liuhe688/article/details/7050868","niceDate":"2016-06-06 11:54","niceShareDate":"未知时间","origin":"CSDN","prefix":"","projectLink":"","publishTime":1465185295000,"realSuperChapterId":9,"selfVisible":0,"shareDate":null,"shareUser":"","superChapterId":10,"superChapterName":"四大组件","tags":[],"title":"基础总结篇之八：创建及调用自己的ContentProvider","type":0,"userId":-1,"visible":1,"zan":0}]
         * offset : -20
         * over : false
         * pageCount : 1
         * size : 20
         * total : 6
         */

        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
        private List<DatasBean> datas;

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isOver() {
            return over;
        }

        public void setOver(boolean over) {
            this.over = over;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DatasBean> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasBean> datas) {
            this.datas = datas;
        }

        public static class DatasBean {
            /**
             * apkLink :
             * audit : 1
             * author : 腾讯Bugly
             * canEdit : false
             * chapterId : 17
             * chapterName : ContentProvider
             * collect : false
             * courseId : 13
             * desc :
             * descMd :
             * envelopePic :
             * fresh : false
             * id : 1323
             * link : https://mp.weixin.qq.com/s/jhVzFa6DatRNK9anuDoSUA
             * niceDate : 2017-10-15 18:24
             * niceShareDate : 未知时间
             * origin :
             * prefix :
             * projectLink :
             * publishTime : 1508063069000
             * realSuperChapterId : 9
             * selfVisible : 0
             * shareDate : null
             * shareUser :
             * superChapterId : 10
             * superChapterName : 四大组件
             * tags : []
             * title : Android 7.0中ContentProvider实现原理
             * type : 0
             * userId : -1
             * visible : 1
             * zan : 0
             */

            private String apkLink;
            private int audit;
            private String author;
            private boolean canEdit;
            private int chapterId;
            private String chapterName;
            private boolean collect;
            private int courseId;
            private String desc;
            private String descMd;
            private String envelopePic;
            private boolean fresh;
            private int id;
            private String link;
            private String niceDate;
            private String niceShareDate;
            private String origin;
            private String prefix;
            private String projectLink;
            private long publishTime;
            private int realSuperChapterId;
            private int selfVisible;
            private Object shareDate;
            private String shareUser;
            private int superChapterId;
            private String superChapterName;
            private String title;
            private int type;
            private int userId;
            private int visible;
            private int zan;
            private List<?> tags;

            public String getApkLink() {
                return apkLink;
            }

            public void setApkLink(String apkLink) {
                this.apkLink = apkLink;
            }

            public int getAudit() {
                return audit;
            }

            public void setAudit(int audit) {
                this.audit = audit;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public boolean isCanEdit() {
                return canEdit;
            }

            public void setCanEdit(boolean canEdit) {
                this.canEdit = canEdit;
            }

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public String getChapterName() {
                return chapterName;
            }

            public void setChapterName(String chapterName) {
                this.chapterName = chapterName;
            }

            public boolean isCollect() {
                return collect;
            }

            public void setCollect(boolean collect) {
                this.collect = collect;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDescMd() {
                return descMd;
            }

            public void setDescMd(String descMd) {
                this.descMd = descMd;
            }

            public String getEnvelopePic() {
                return envelopePic;
            }

            public void setEnvelopePic(String envelopePic) {
                this.envelopePic = envelopePic;
            }

            public boolean isFresh() {
                return fresh;
            }

            public void setFresh(boolean fresh) {
                this.fresh = fresh;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getNiceDate() {
                return niceDate;
            }

            public void setNiceDate(String niceDate) {
                this.niceDate = niceDate;
            }

            public String getNiceShareDate() {
                return niceShareDate;
            }

            public void setNiceShareDate(String niceShareDate) {
                this.niceShareDate = niceShareDate;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public String getPrefix() {
                return prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }

            public String getProjectLink() {
                return projectLink;
            }

            public void setProjectLink(String projectLink) {
                this.projectLink = projectLink;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public int getRealSuperChapterId() {
                return realSuperChapterId;
            }

            public void setRealSuperChapterId(int realSuperChapterId) {
                this.realSuperChapterId = realSuperChapterId;
            }

            public int getSelfVisible() {
                return selfVisible;
            }

            public void setSelfVisible(int selfVisible) {
                this.selfVisible = selfVisible;
            }

            public Object getShareDate() {
                return shareDate;
            }

            public void setShareDate(Object shareDate) {
                this.shareDate = shareDate;
            }

            public String getShareUser() {
                return shareUser;
            }

            public void setShareUser(String shareUser) {
                this.shareUser = shareUser;
            }

            public int getSuperChapterId() {
                return superChapterId;
            }

            public void setSuperChapterId(int superChapterId) {
                this.superChapterId = superChapterId;
            }

            public String getSuperChapterName() {
                return superChapterName;
            }

            public void setSuperChapterName(String superChapterName) {
                this.superChapterName = superChapterName;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public int getZan() {
                return zan;
            }

            public void setZan(int zan) {
                this.zan = zan;
            }

            public List<?> getTags() {
                return tags;
            }

            public void setTags(List<?> tags) {
                this.tags = tags;
            }
        }
    }
}
