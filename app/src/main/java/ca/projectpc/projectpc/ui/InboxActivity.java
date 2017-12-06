package ca.projectpc.projectpc.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.projectpc.projectpc.R;
import ca.projectpc.projectpc.api.IServiceCallback;
import ca.projectpc.projectpc.api.Service;
import ca.projectpc.projectpc.api.ServiceResult;
import ca.projectpc.projectpc.api.services.MessageService;

public class InboxActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup swipe refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.inbox_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(
                R.color.colorAccent,
                R.color.colorPrimary
        );

        // TODO: Fix this activity,
        // it won't open (It has to do with the recycler view in the other activity)

        // Setup recycler view
        //mRecyclerView = (RecyclerView) findViewById(R.id.inbox_threads_recycler_view);

        // Refresh messages
        //refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void refresh() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);

            MessageService service = Service.get(MessageService.class);
            service.getAllMessages(new IServiceCallback<MessageService.GetMessagesResult>() {
                @Override
                public void onEnd(ServiceResult<MessageService.GetMessagesResult> result) {
                    mSwipeRefreshLayout.setRefreshing(false);

                    if (!result.hasError()) {
                        // TODO: Add messages to recycler view depending on them existing
                        // TODO: previously or not
                    } else {
                        result.getException().printStackTrace();

                        Toast.makeText(getBaseContext(), "Unable to refresh messages",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();

            Toast.makeText(this, "Unable to refresh messages",
                    Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            private CardView mCardView;
            private TextView mMessageText;
            private TextView mMessageSender;
            private TextView mMessageTimeReceived;

            public ViewHolder(View itemView) {
                super(itemView);

                mCardView = (CardView) itemView.findViewById(R.id.item_inbox_card_view);
                mMessageText = (TextView) itemView.findViewById(R.id.item_inbox_message);
                mMessageSender = (TextView) itemView.findViewById(R.id.item_inbox_sender);
                mMessageTimeReceived =
                        (TextView) itemView.findViewById(R.id.item_inbox_time_received);
            }
        }

        private List<MessageService.Message> mMessages;

        public MessageAdapter(List<MessageService.Message> messages) {
            mMessages = messages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_inbox_thread, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final MessageService.Message message = mMessages.get(position);

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Start MessageActivity for message thread
                }
            });

            // Setup text
            //holder.mMessageSender.setText(message.)
        }

        @Override
        public int getItemCount() {
            return mMessages.size();
        }
    }
}