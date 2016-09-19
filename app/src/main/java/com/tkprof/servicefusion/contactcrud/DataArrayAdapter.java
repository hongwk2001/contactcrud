package com.tkprof.servicefusion.contactcrud;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class DataArrayAdapter extends ArrayAdapter<ContactRecord> {
	private final Activity context;
	private final List<ContactRecord> values;
	ViewHolder viewHolder ;

	static class ViewHolder {
		    public TextView tv_first_name;
		    public TextView tv_last_name;
		    public TextView tv_date_of_birth;
		    public TextView tv_zip_code;
	  }

	  public DataArrayAdapter(Activity context, List<ContactRecord> values) {
	    super(context, R.layout.rowlayout, values);
	    this.context = context;
	    this.values = values;
	  }


	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	     View rowView = convertView;
	     // configure view holder
	      
	    // reuse views
	    if (rowView == null  || viewHolder.tv_first_name == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.rowlayout, null);
	      viewHolder = new ViewHolder();
	      viewHolder.tv_first_name = (TextView) rowView.findViewById(R.id.tv_datetime);
	      viewHolder.tv_last_name = (TextView) rowView.findViewById(R.id.tv_att_avg);
	      viewHolder.tv_date_of_birth = (TextView) rowView.findViewById(R.id.tv_med_avg);
	      viewHolder.tv_zip_code = (TextView) rowView.findViewById(R.id.tv_minutes);
	      rowView.setTag(viewHolder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();

		holder.tv_first_name.setText( values.get(position).getFirstName());
		holder.tv_last_name.setText(""+values.get(position).getLastName() );
		holder.tv_date_of_birth.setText(""+values.get(position).getDateOfBirth());
		holder.tv_zip_code.setText(""+values.get(position).getZipCode());
	     
	    return rowView;
	  }
}
