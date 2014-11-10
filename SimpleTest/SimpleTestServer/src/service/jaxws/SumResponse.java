package service.jaxws;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "sumResponse", namespace = "http://service/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sumResponse", namespace = "http://service/")
public class SumResponse {

    @XmlElement(name = "return", namespace = "")
    private int _return;

    /**
     * @return returns int
     */
    public int getReturn() {
        return this._return;
    }

    /**
     * @param _return the value for the _return property
     */
    public void setReturn(int _return) {
        this._return = _return;
    }

}
