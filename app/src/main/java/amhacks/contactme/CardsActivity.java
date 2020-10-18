package amhacks.contactme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CardsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef,cardsRef;
    private String currentUserID,user,fullname,designation,organisation,phone;
    private TextView no_mesg;
    private RecyclerView CardsView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        no_mesg = (TextView) findViewById(R.id.no_cards_text);
        CardsView = (RecyclerView) findViewById(R.id.cards_recycler_view);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        cardsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Cards");

        cardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    no_mesg.setVisibility(View.GONE);
                    CardsView.setVisibility(View.VISIBLE);
                }
                else
                {
                    no_mesg.setVisibility(View.VISIBLE);
                    CardsView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<Cards> options =
                new FirebaseRecyclerOptions.Builder<Cards>()
                        .setQuery(cardsRef,Cards.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cards,CardsViewHolder>(options)
        {

            @NonNull
            @Override
            public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cards_view_layout,parent,false);

                view.findViewById(R.id.call_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = "tel:" + phone;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);

                    }
                });


                return new CardsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final CardsViewHolder holder, int position, @NonNull Cards model) {
                user = getRef(position).getKey();
                usersRef.child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            fullname = snapshot.child("fullname").getValue().toString();
                            designation = snapshot.child("designation").getValue().toString();
                            organisation = snapshot.child("organisation").getValue().toString();
                            phone = snapshot.child("phone").getValue().toString();

                            holder.setName(fullname);
                            holder.setDesg("Designation: "+designation);
                            holder.setPh("Phone: "+phone);
                            holder.setOrg("Organisation: "+organisation);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        CardsView.setLayoutManager(linearLayoutManager);
        CardsView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.startListening();
    }

    class CardsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public CardsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name)
        {
            TextView tv = mView.findViewById(R.id.card_fullname);
            tv.setText(name);
        }
        public void setDesg(String desg)
        {
            TextView tv = mView.findViewById(R.id.card_design);
            tv.setText(desg);
        }
        public void setOrg(String org)
        {
            TextView tv = mView.findViewById(R.id.card_org);
            tv.setText(org);
        }
        public void setPh(String ph)
        {
            TextView tv = mView.findViewById(R.id.card_phone_number);
            tv.setText(ph);
        }
    }
}