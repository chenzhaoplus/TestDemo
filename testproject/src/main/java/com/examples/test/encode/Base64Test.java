package com.examples.test.encode;

import com.examples.test.util.Base64Utils;

/**
 * @Author: cz
 * @Date: 2020/7/14
 * @Description:
 */
public class Base64Test {

    public static void main(String[] args) {
//        String path = "http://59.173.19.66:10000/M00/23/2A/rBAE1l6DYuuAfKyxAAApHFsHnTE.288e6c";
//        String path = "M00/06/4B/rBCZBl2JYCSAQHwCAAAAAAAAAAA310.jpg";
        String path = "group1/M00/A2/A9/rBAEUl8Wp9SABSRSAADS0AQFX3A.142c46";
        String encode = Base64Utils.encode(path);
        System.out.println(encode);

        String encde = "AID6RAAAoEEvRpa9nTrsPTqWfb0frQ88vf76vccCZr1hV3i97tKkPVLpzj0RbS29jUmhPQWtD73p/aS9NcicPPL03bxnwYW83b42vG81Db0urky9DiCjvd/uULu+CgA9pWCovSlEfrxpuXU9dnuOPQcQ97xTND+88VXDvXB+0L0vT8u8ztorPRBjVzzNMKM8h7kXvfbpYD1O0Be8a6VjPT5WAD4HVb28CBMjvShaZz1xnNQ7y4sGPquFPLw5OLI9qfWsPSRKgDz9b5S9swMYvvhXHb5nUga+uvTEvU9qCr1aAQg+yGdBvSPvuTwOloK8UAOiPKnkCT5NCpK8zoPFPYGfar2aKrW8yx0uPCHM+LvxOAQ9d98avQt6aL24HxM9c86uO3N4trx/apa817z+vfkPPD3eq3a7ob6BPQ5Iz7w7W6S9mwCMPZi7V72VuUY9/0EGPFlNob2ioe08f7DbPB8qsr0A4Cc+ISuiPT+1Zb2Vrk+97oVKPY4ISrzWFqy9/mCMvSt+dL3eukG8d84ZPdanEz1gRT49h1COPVZB1ry5bXU935KbOyaTAbzvTBO7j4EXvcj0+r3Ak7+9yX+8PIblHz1mc1q8kw3TPGKngz1WZr89NyejPYG1Rz3N0Zy996GoPY2AubxmkgQ9Q2g8vd6av7y9LAK7CjF9vQD4xD01Bcq9lDSRvMHA3z1XThw72MB3O7//Db26jqA9et/dPUctKT49Hby9KDRhPej8ozxQl3m9rZxFPHqfjzwZTHI9dsCGPUGRij2faAq9K61RvUv3Vb0Vjky9E4LUPIdDwjwPYU46yG7nPUIqj72KT2K9se9tvHvZdjz/GCE7N57jPPDts7yhq4i9swCHvE4WjTy7sss9vdUnPWDvyb1bBUg9H1ODvAqfTr39cMQ9WjuIPHFYmL01FVU9aFurvW3LbTzVlyu9UqFhPaP4Xj3K43k9KWzBvFnKuzsPpaS9ggXaPF2dp71SEo289prFPa6+5TxJBJS9h3aUPdZTZb3ZWqM96rAdvgqGpL1OcjW9/42nPSVGeTuFuye9Ua2WvR+Ypb3Zyco8/roDPkeJvLzeiG+9ok4iPfi/NT1l6xy9PXCPvKeTpz2JIva9snhtPcWWkL2P0a68MzWwvDHJEr2eqi49iAvlPOdlu73bwUs9lkCnvNLuB75juuG8bjMUvRfbMj2Fb6G8RhxlPFpOeDwKysY94LaEvSAXV71bYLw9g72ePDAal7z/1sA9fyiBvPs9Hb0+tuS7kseBvMLrUL2yuSI+pXETPDLKHj2VPLY8svWMPUAUHb3b7eC8Atk6PQL5ET274hS+33EFPinHV73F5788cuDTOwLWkT0TAr89dpTHvJZ8x7sBN5g8";
        byte[] decode = Base64Utils.decode(encde);
        System.out.println(decode);
    }

}
