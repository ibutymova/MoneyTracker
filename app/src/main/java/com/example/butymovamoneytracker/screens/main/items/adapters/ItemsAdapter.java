package com.example.butymovamoneytracker.screens.main.items.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.data.db.ItemEntity;
import com.example.butymovamoneytracker.screens.main.items.ItemsFragmentViewModel;
import com.example.butymovamoneytracker.states.SelectedItems;
import com.example.butymovamoneytracker.utils.FormatUtils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ItemsFragmentViewModel viewModel;
    private FormatUtils formatUtils;
    private SelectedItems selectedItems;
    private List<ItemEntity> items = Collections.emptyList();

    @Inject
    public ItemsAdapter(FormatUtils formatUtils, SelectedItems selectedItems) {
        this.formatUtils = formatUtils;
        this.selectedItems = selectedItems;
        this.setHasStableIds(true);
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public void setViewModel(ItemsFragmentViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).bind(items.get(position), selectedItems.getItems().containsValue(items.get(position).getId()), viewModel, formatUtils);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        @BindView(R.id.tvDate)
        TextView tvDate;

        Unbinder unbinder;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }

        void bind(final ItemEntity item, final Boolean selected, final ItemsFragmentViewModel viewModel, final FormatUtils formatUtils){
            tvName.setText(item.getName());
            tvPrice.setText(itemView.getContext().getResources().getString(R.string.price_format, formatUtils.getPriceStr(item.getPrice()), itemView.getContext().getResources().getString(R.string.RUB)));
            tvDate.setText(formatUtils.getDateStr(item.getCreated_at()));
            itemView.setSelected(selected);

            itemView.setOnClickListener(v -> {
                if (viewModel != null)
                    viewModel.onClickItem(getLayoutPosition(), item.getId());
            });

            itemView.setOnLongClickListener(v -> {
                if (viewModel!=null)
                    viewModel.onLongClickItem(getLayoutPosition(), item.getId());
                return true;
            });
        }
   }
}
