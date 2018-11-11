package joaopaulo.acompanhamentodiario.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.List;

/**
 * Created by JoaoPaulo on 20/07/2015.
 */
public abstract class GridViewAdapter<Model> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<Model> items;

    public GridViewAdapter(List<Model> items, Context context) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Model getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    protected abstract GridView getGridView();

    public void add(Model item){
        this.items.add(item);
        calcGridViewHeight(this.getGridView());
        notifyDataSetChanged();
    }

    public void calcGridViewHeight(GridView gridView) {
        int rowHeight = 93;
        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.height = rowHeight * items.size();
        gridView.setLayoutParams(layoutParams);
    }
}
