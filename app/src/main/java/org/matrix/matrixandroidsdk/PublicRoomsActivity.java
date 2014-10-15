package org.matrix.matrixandroidsdk;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.matrix.androidsdk.MXData;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.api.response.PublicRoom;
import org.matrix.androidsdk.api.response.login.Credentials;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.MXMemoryStore;
import org.matrix.androidsdk.rest.client.EventsApiClient;
import org.matrix.matrixandroidsdk.adapters.RoomsAdapter;

import java.util.List;


public class PublicRoomsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_rooms);

        // FIXME Should be reading from a single one
        Credentials creds = new Credentials();
        creds.homeServer = "matrix.org";
        final MXSession matrixSession = new MXSession(new MXData(new MXMemoryStore()), creds);

        final GridView publicRoomsGridView = (GridView) findViewById(R.id.gridView_publicRoomList);
        final RoomsAdapter adapter = new RoomsAdapter(this, R.layout.adapter_item_public_rooms);
        adapter.setAlternatingColours(0xFFFFFFFF, 0xFFEEEEEE);
        publicRoomsGridView.setAdapter(adapter);
        matrixSession.getEventsApiClient().loadPublicRooms(new EventsApiClient.LoadPublicRoomsCallback() {
            @Override
            public void onRoomsLoaded(List<PublicRoom> publicRooms) {
                for (PublicRoom publicRoom : publicRooms) {
                    adapter.add(publicRoom);
                }
            }
        });

        publicRoomsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RoomState room = adapter.getItem(i);
                String roomId = room.roomId;
                goToRoom(roomId);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.public_rooms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_my_rooms) {
            goToHomepage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToHomepage() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void goToRoom(String roomId) {
        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId);
        startActivity(intent);
    }
}
