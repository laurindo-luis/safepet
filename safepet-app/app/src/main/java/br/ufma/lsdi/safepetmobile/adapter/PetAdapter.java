package br.ufma.lsdi.safepetmobile.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufma.lsdi.safepetmobile.CONSTANTES;
import br.ufma.lsdi.safepetmobile.R;
import br.ufma.lsdi.safepetmobile.domain.Pet;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<Pet> pets;
    private final PetAdapterClickListener onClickListener;

    public interface PetAdapterClickListener {
        void onClickMenu(View view, int index);
    }

    public PetAdapter(List<Pet> pets, PetAdapterClickListener onClickListener) {
        this.pets = pets;
        this.onClickListener = onClickListener;
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName, textViewIdColeira, textViewType;
        private final ImageView imagePet;
        private final CardView cardView;

        public PetViewHolder(View view) {
            super(view);

            textViewName = view.findViewById(R.id.textViewName);
            textViewIdColeira = view.findViewById(R.id.textViewIdColeira);
            textViewType = view.findViewById(R.id.textViewType);
            imagePet = view.findViewById(R.id.imagePet);
            cardView = view.findViewById(R.id.card_view);

        }

        public TextView getTextViewName() {
            return textViewName;
        }

        public TextView getTextViewIdColeira() {
            return textViewIdColeira;
        }

        public TextView getTextViewType() {
            return textViewType;
        }

        public ImageView getImagePet() {
            return imagePet;
        }

        public CardView getCardView() {
            return cardView;
        }

    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_pet, viewGroup, false);

        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder viewHolder, final int position) {

        Pet pet = pets.get(position);

        Integer imageOption = null;
        String typePet = "";

        if(pet.getType() == 0) {
            imageOption = R.drawable.cat_adapter;
            typePet = "Gato";
        } else if(pet.getType() == 1) {
            imageOption = R.drawable.dog_adapter;
            typePet = "Cachorro";
        }


        viewHolder.getImagePet().setImageResource(imageOption);
        viewHolder.getTextViewName().setText(pet.getName());
        viewHolder.getTextViewIdColeira().setText(String.format("ID coleira: %s", pet.getIdColeira()));
        viewHolder.getTextViewType().setText(String.format("Tipo: %s", typePet));
        if(pet.isMonitored() == false)
            viewHolder.getCardView().setCardBackgroundColor(Color.RED);

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
        return pets.size();
    }

}
