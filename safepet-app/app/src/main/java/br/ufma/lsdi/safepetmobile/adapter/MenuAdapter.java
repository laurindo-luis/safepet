package br.ufma.lsdi.safepetmobile.adapter;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.ufma.lsdi.safepetmobile.CONSTANTES;
import br.ufma.lsdi.safepetmobile.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Integer[] options;
    private final MenuAdapterClickListener onClickListener;

    public interface MenuAdapterClickListener {
        void onClickMenu(View view, int index);
    }

    public MenuAdapter(Integer[] options, MenuAdapterClickListener onClickListener) {
        this.options = options;
        this.onClickListener = onClickListener;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final ImageView imageView;

        public MenuViewHolder(View view) {
            super(view);

            textViewName = view.findViewById(R.id.textViewName);
            imageView = view.findViewById(R.id.imageMenu);

        }

        public TextView getTextViewName() {
            return textViewName;
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_menu, viewGroup, false);

        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder viewHolder, final int position) {

        Integer op = options[position];

        String nameOption = null;
        Integer imageOption = null;

        if(op == CONSTANTES.USER) {
            nameOption = "Perfil";
            imageOption = R.drawable.user;

        } else if(op == CONSTANTES.MAP) {
            nameOption = "Mapa";
            imageOption = R.drawable.map;
        } else if(op == CONSTANTES.PETS) {
            nameOption = "Pets";
            imageOption = R.drawable.pet;
        }

        viewHolder.getTextViewName().setText(nameOption);
        viewHolder.getImageView().setImageResource(imageOption);

        if(onClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickMenu(viewHolder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return options.length;
    }

}
