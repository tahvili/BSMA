/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * showing the data from database
 */
package info.androidhive.bsma.activity;
import info.androidhive.bsma.R;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.os.AsyncTask;


public class ListAdapter extends BaseAdapter {

    Context context;
    List<Subject> valueList;

    public ListAdapter(List<Subject> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        if(valueList!=null && valueList.size() > 0 )
        {

        return this.valueList.size();}
        else {
        return 0;}
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.listview_item, null);

            viewItem.TextViewEvents = (TextView)convertView.findViewById(R.id.textView1);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }
if(valueList.get(position).Subject_title!=null) {

    viewItem.TextViewEvents.setText(valueList.get(position).Subject_title);
}
else {viewItem.TextViewEvents.setText("Please load the page again");}
        return convertView;
    }
}

class ViewItem
{
    TextView TextViewEvents;

}


