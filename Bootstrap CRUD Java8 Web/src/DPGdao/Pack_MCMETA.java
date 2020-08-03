/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPGdao;

/**
 *
 * @author utrer
 */
public class Pack_MCMETA {

    //Esto indica los nombres de las diversas versiones, para poder crear los distintos JSON con la informaciaon de
    //datapacak mcmeta
    public class PackInfo {

        public final static int V1_15 = 5, V1_14 = 4;

        private int pack_format;
        private String description;

        public PackInfo(String desc) {
            pack_format = V1_15;
            description = desc;
        }

        public PackInfo(int format, String desc) {
            this(desc);
            pack_format = format;
        }
    }

    private PackInfo pack;

    public Pack_MCMETA(String desc) {
        pack = new PackInfo(desc);
    }

    public Pack_MCMETA(int format, String desc) {
        pack = new PackInfo(format, desc);
    }

}
