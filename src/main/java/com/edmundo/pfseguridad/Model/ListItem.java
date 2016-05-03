package com.edmundo.pfseguridad.Model;

import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**
 * Created by Edmundo on 10/21/15.
 */
public class ListItem extends RecyclerView.ViewHolder{
    public LinearLayout linearLayout;


    public ListItem(LinearLayout linearLayout) {
        super(linearLayout);
        this.linearLayout = linearLayout;
    }
}
