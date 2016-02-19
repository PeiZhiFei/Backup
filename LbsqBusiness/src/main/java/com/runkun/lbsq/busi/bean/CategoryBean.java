package com.runkun.lbsq.busi.bean;

import java.util.List;

public class CategoryBean
{
    /**
     * hasChildren : 1
     * class_id : 153
     * subclass : [{"class_id":"156","parent_class_id":"153","class_name":"调味品"},{"class_id":"158","parent_class_id":"153","class_name":"方便食品"}]
     * parent_class_id : 0
     * class_name : 粮油调味
     */
    private boolean hasChildren;
    private String class_id;
    private List<SubclassEntity> subclass;
    private String parent_class_id;
    private String class_name;

    public void setHasChildren(boolean hasChildren)
    {
        this.hasChildren = hasChildren;
    }

    public void setClass_id(String class_id)
    {
        this.class_id = class_id;
    }

    public void setSubclass(List<SubclassEntity> subclass)
    {
        this.subclass = subclass;
    }

    public void setParent_class_id(String parent_class_id)
    {
        this.parent_class_id = parent_class_id;
    }

    public void setClass_name(String class_name)
    {
        this.class_name = class_name;
    }

    public boolean getHasChildren()
    {
        return hasChildren;
    }

    public String getClass_id()
    {
        return class_id;
    }

    public List<SubclassEntity> getSubclass()
    {
        return subclass;
    }

    public String getParent_class_id()
    {
        return parent_class_id;
    }

    public String getClass_name()
    {
        return class_name;
    }

    public class SubclassEntity
    {
        /**
         * class_id : 156
         * parent_class_id : 153
         * class_name : 调味品
         */
        private String class_id;
        private String parent_class_id;
        private String class_name;

        public void setClass_id(String class_id)
        {
            this.class_id = class_id;
        }

        public void setParent_class_id(String parent_class_id)
        {
            this.parent_class_id = parent_class_id;
        }

        public void setClass_name(String class_name)
        {
            this.class_name = class_name;
        }

        public String getClass_id()
        {
            return class_id;
        }

        public String getParent_class_id()
        {
            return parent_class_id;
        }

        public String getClass_name()
        {
            return class_name;
        }
    }
}
