package egovframework.example.map.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class MapVO {
 
    @XmlElement(name = "gid")
    private String gid = "";
    @XmlElement(name = "content")
    private String content = "";
    @XmlElement(name = "title")
    private String title = "";
     
    public String getGid() {
        return gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
     
}
