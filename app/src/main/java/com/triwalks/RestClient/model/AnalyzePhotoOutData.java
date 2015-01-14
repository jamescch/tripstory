package com.triwalks.RestClient.model;

import java.util.List;

/**
 * Created by tutul on 2015/1/10.
 */
public class AnalyzePhotoOutData {
    String information;
    List<PhotoTree> photo_tree;
    Boolean result;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public List<PhotoTree> getPhoto_tree() {
        return photo_tree;
    }

    public void setPhoto_tree(List<PhotoTree> photo_tree) {
        this.photo_tree = photo_tree;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public class PhotoTree
    {
        List<group> grouplist;
        int level;

        public List<group> getGrouplist() {
            return grouplist;
        }

        public void setGrouplist(List<group> grouplist) {
            this.grouplist = grouplist;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public class group
        {
            String deputy;
            int groupID;
            location location;
            List<String> photolist;

            public String getDeputy() {
                return deputy;
            }

            public void setDeputy(String deputy) {
                this.deputy = deputy;
            }

            public Integer getGroupID() {
                return groupID;
            }

            public void setGroupID(Integer groupID) {
                this.groupID = groupID;
            }

            public group.location getLocation() {
                return location;
            }

            public void setLocation(group.location location) {
                this.location = location;
            }

            public List<String> getPhotolist() {
                return photolist;
            }

            public void setPhotolist(List<String> photolist) {
                this.photolist = photolist;
            }

            public class location
            {
                double x;
                double y;

                location(double _x,double _y)
                {
                    x = _x;
                    y = _y;
                }
                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }
            }
        }
    }
}
