package christmas.hotvideocallprank.christmassantavideocall.snapchat.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotvideocallprank.christmassantavideocall.R;

public class AFLayout extends RelativeLayout implements View.OnClickListener {

    public RelativeLayout afParentLayout;
    public ImageView icon;
    public TextView title, description;
    public Button install, getMore;

    public AFLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.af_layout, this, true);
        createView(view);
    }

    public AFLayout(Context context) {
        this(context, null);
    }

    private void createView(View view) {
        afParentLayout = view.findViewById(R.id.af_parent);
        icon = view.findViewById(R.id.ad_icon);
        title = view.findViewById(R.id.ad_title);
        description = view.findViewById(R.id.ad_desc);
        install = view.findViewById(R.id.ad_install);
        getMore = view.findViewById(R.id.ad_get_more);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    @Override
    public void onClick(View view) {
        if (view == install) {

        } else if (view == getMore) {

        }
    }
}
