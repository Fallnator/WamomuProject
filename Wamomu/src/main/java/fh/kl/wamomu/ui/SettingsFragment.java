package fh.kl.wamomu.ui;

import android.app.Dialog;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;

import java.util.Set;

import fh.kl.wamomu.R;
import fh.kl.wamomu.dialogs.dialog_information;

/**
 * Created by Thundernator on 04.11.13.
 */
public class SettingsFragment extends Fragment {

    private TextView pairview, infoview, kalview, codeview;
    private EditText codepin;
    private Button searchButton;
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings,
                container, false);
        getActivity().setTitle("Einstellungen");

        pairview = (TextView) view.findViewById(R.id.pair_textView);
        infoview = (TextView) view.findViewById(R.id.info_textView);
        kalview = (TextView) view.findViewById(R.id.add_textView);

        codeview = (TextView) view.findViewById(R.id.code);


        kalview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog2 = new Dialog(getActivity());

                dialog2.setContentView(R.layout.dialog_add_geraet);
                dialog2.setTitle("Neues Gerät hinzufügen");
                dialog2.setCancelable(true);



                codepin = (EditText) dialog2.findViewById(R.id.pinset);
                searchButton = (Button) dialog2.findViewById(R.id.btnSearch);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        codeview.setText(codepin.getText().toString());
                       dialog2.dismiss();
                   }
                });

                dialog2.show();
            }
        });

        pairview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAdapter = BluetoothAdapter.getDefaultAdapter();
                Log.d("\nAdapter: ", "" + btAdapter);

                CheckBluetoothState();
            }
        });

        infoview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final dialog_information dialog1 = new dialog_information(getActivity());

                dialog1.show();

            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBluetoothState();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void CheckBluetoothState() {
        // Checks for the Bluetooth support and then makes sure it is turned on
        // If it isn't turned on, request to turn it on
        // List paired devices
        if(btAdapter==null) {
            pairview.append("\nBluetooth NOT supported. Aborting.");
            return;
        } else {
            if (btAdapter.isEnabled()) {
                Log.d("Bluetooth is enabled...", "" );
                pairview.setText("");

                // Listing paired devices
                pairview.append("\nPaired Devices are:");
                Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {

                    pairview.append("\n  Device: " + device.getName() + ", " + device);
                }
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

}

