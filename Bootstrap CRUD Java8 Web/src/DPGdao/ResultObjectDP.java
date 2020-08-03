/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPGdao;

/**
 * Esta clase representa al elemento "result" del DataPack
 *
 * @author usuario
 */
public class ResultObjectDP {

    private String item;
    /*
    Al se Integer y llamar al constructor que solo recibe el nombre del item
    GSON no creara el par clave valor "count":X donde X es un numero
     */
    private Integer count;

    public ResultObjectDP(String item) {
        this.item = Item.NAMESPACE + item;
    }

    public ResultObjectDP(String item, int count) {
        this.item = Item.NAMESPACE + item;
        this.count = count;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
