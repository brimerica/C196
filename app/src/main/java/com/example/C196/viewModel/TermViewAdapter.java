package com.example.C196.viewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.C196.R;
import com.example.C196.models.Term;
import com.example.C196.utilities.TextFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TermViewAdapter extends RecyclerView.Adapter<TermViewAdapter.TermViewHolder> {

    private List<Term> terms = new ArrayList<>();
    private OnTermClickListener listener;

//    public TermViewAdapter() {
//        super(DIFF_CALLBACK);
//    }
//
//    private static final DiffUtil.ItemCallback<Term> DIFF_CALLBACK = new DiffUtil.ItemCallback<Term>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
//            return oldItem.getId() == newItem.getId();
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
//            return oldItem.getTitle().equals(newItem.getTitle()) &&
//                    oldItem.getEndDate().equals(newItem.getEndDate()) &&
//                    oldItem.getStartDate().equals(newItem.getStartDate());
//        }
//    };

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View termItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent,false);
        return new TermViewHolder(termItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        Term currentTerm = terms.get(position);
        //Term currentTerm = getItem(position);
        String startDateFormatted = TextFormatter.appFormat.format(currentTerm.getStartDate());
        String endDateFormatted = TextFormatter.appFormat.format(currentTerm.getEndDate());
        holder.textViewTitle.setText(currentTerm.getTitle());
        holder.textViewDates.setText(startDateFormatted + " - " + endDateFormatted);
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void setTerms(List<Term> terms){
        this.terms = terms;
        //temporary - no animations - need to replace
        notifyDataSetChanged();
    }

    public Term getTermAt(int position) {
        return terms.get(position);
        //return getItem(position);
    }

    public class TermViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDates;
        private ImageView editTermBtn;

        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.term_view_title);
            textViewDates = itemView.findViewById(R.id.term_view_dates);
            editTermBtn = itemView.findViewById(R.id.edit_term_btn);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onTermClick(terms.get(position));
                        //listener.onTermClick(getItem(position));
                    }
                }
            });

            editTermBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onEditTermClick(terms.get(position));
                        //listener.onEditTermClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnTermClickListener {
        void onTermClick(Term term);
        void onEditTermClick(Term term);
    }

    public void setOnTermClickListener(OnTermClickListener listener) {
        this.listener = listener;
    }

}
