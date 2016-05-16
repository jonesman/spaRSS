/**
 * spaRSS
 * <p>
 * Copyright (c) 2015 Arnaud Renaud-Goud
 * Copyright (c) 2012-2015 Frederic Julian
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.etuldan.sparss.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import net.etuldan.sparss.Constants;
import net.etuldan.sparss.MainApplication;
import net.etuldan.sparss.R;
import net.etuldan.sparss.provider.FeedData.FeedColumns;
import net.etuldan.sparss.utils.UiUtils;

import org.w3c.dom.Text;

public class GroupsCursorAdapter extends ResourceCursorAdapter {

    private int mNamePos = -1;
    private int mGroupIdPos = -1;
    private int mIdPos = -1;
    private Activity activity;

    public GroupsCursorAdapter(Activity activity, int layout, Cursor gc) {
        super(activity, layout, gc, 0);
        this.activity = activity;
        getCursorPositions(gc);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if (view.getTag()!=null && view.getTag().equals("empty")) {
            //((TextView)view).setText("asdf");
            return;
        }

        TextView v = (TextView) view;
        v.setText(cursor.getString(mNamePos));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position==0 && convertView == null) {
            convertView = newView(activity, getCursor(), parent);
            convertView.setTag("empty");
        }

        View v = super.getView(position > 0 ? position - 1 : position,
                convertView, parent);

        return v;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if(position==0 && convertView == null) {
            convertView = newDropDownView(activity, getCursor(), parent);
            convertView.setTag("empty");
        }

        View v = super.getDropDownView(position > 0 ? position - 1 : position,
                convertView, parent);

        return v;

    }

    @Override
    public int getCount() {
        return super.getCount()+1;
    }

    @Override
    public long getItemId(int position) {
        if(position>=1) {
            return super.getItemId(position -1);
        }
        return -1;
    }

    public int getPositionForGroupId(long gid) {
        while(getCursor().moveToNext()) {
            if(gid==getCursor().getLong(mIdPos)) {
                return getCursor().getPosition()+1;
            }
        }
        return -1;
    }

    /*@Override
    public void notifyDataSetChanged() {
        getCursorPositions(null);
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        getCursorPositions(null);
        super.notifyDataSetInvalidated();
    }*/

    private synchronized void getCursorPositions(Cursor cursor) {
        if (cursor != null && mGroupIdPos == -1) {
            mGroupIdPos = cursor.getColumnIndex(FeedColumns.GROUP_ID);
            mNamePos = cursor.getColumnIndex(FeedColumns.NAME);
            mIdPos = cursor.getColumnIndex(FeedColumns._ID);
        }
    }

}
