package DPGdao;

/**
 * Esta clase representa al elemnto "key" de la estructura JSON que conforma un
 * datapack
 *
 * El elemento "key" tiene la siguiente estructura
 *
 * "key": { "clave1": { "item": "minecraft:XXX" }, "clave2": { "tag":
 * "minecraft:YYY" } }
 *
 *
 * @author jdreyes
 */
public class KeyObjectDP {

    /**
     * Variables de instancia
     */
    private String item;
    private String tag;

    public KeyObjectDP(Item item) {
        if (item.isATagItem()) {
            this.tag = item.getName();
        } else {
            this.item = item.getName();
        }
    }

    public String getItem() {
        return item;
    }

    public String getPureItem() {
        if (item == null) {
            return tag.substring(Item.NAMESPACE.length());

        } else {
            return item.substring(Item.NAMESPACE.length());
        }
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "KeyObjectDP{" + "item=" + item + '}';
    }

}// Fin clase
