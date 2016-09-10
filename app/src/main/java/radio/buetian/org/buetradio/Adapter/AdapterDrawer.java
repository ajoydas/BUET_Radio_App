package radio.buetian.org.buetradio.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.pkmmte.view.CircularImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

import radio.buetian.org.buetradio.Objects.Information;
import radio.buetian.org.buetradio.R;


public class AdapterDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Information> data= Collections.emptyList();
    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;
    private LayoutInflater inflater;
    FirebaseAuth mAuth;
    CircularImageView imageView;
    Bitmap bm;

    private Context context;
    public AdapterDrawer(Context context, List<Information> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            mAuth= FirebaseAuth.getInstance();
            View view=inflater.inflate(R.layout.drawer_header, parent,false);
            imageView= (CircularImageView) view.findViewById(R.id.image_profile);
            TextView headername = (TextView) view.findViewById(R.id.headername);
            if(mAuth.getCurrentUser()!=null) {
                headername.setText(mAuth.getCurrentUser().getDisplayName());
                //imageView.setImageBitmap();
                if(isNetworkAvailable(context))
                {
                    new LoadImage().execute();
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_google);
                }
            }
            else {
                headername.setText("Viewer");

            }
            HeaderHolder holder=new HeaderHolder(view);
            return holder;
        }
        else{
            View view=inflater.inflate(R.layout.item_drawer, parent,false);
            ItemHolder holder=new ItemHolder(view);
            return holder;
        }

    }
    public boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    private class LoadImage extends AsyncTask
    {


        @Override
        protected Object doInBackground(Object[] objects) {
            bm=getImageBitmap(mAuth.getCurrentUser().getPhotoUrl().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            imageView.setImageBitmap(bm);
        }
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            System.out.println("Image Can't be loaded");
            bm=BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_facebook);
}

return bm;
        }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder ){

        }
        else{
            ItemHolder itemHolder= (ItemHolder) holder;
            Information current=data.get(position-1);
            itemHolder.title.setText(current.title);
            itemHolder.icon.setImageResource(current.iconId);
        }

    }
    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public ItemHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.listText);
            icon= (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);

        }
    }


}
