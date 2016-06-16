package com.news.kikar.biosense;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.OverScroller;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends Activity
        implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback{

    private Button switchActivityBtn;
    private Button switchActivityBtn1;

    //Parser parser = new Parser();
    //BuildMsg buildMsg = new BuildMsg();
    ArrayList<Byte> inputBuffer = new ArrayList<Byte>();
    ArrayList<Byte> outMsgBuffer;

    private ArrayList<Short>shortToRec=new ArrayList<Short>();
    private ArrayList<Short>shortToSend=new ArrayList<Short>();
    private NfcAdapter mNfcAdapter;
    private EditText txtFileName;
    private byte[] fileBuffer;
    private ArrayList<Byte> recBytes=new  ArrayList<Byte>();
    private  int index=0;
    private OverScroller overScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchActivityBtn = (Button) findViewById(R.id.patientS);
        switchActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedStartActivity();
            }
        });

        switchActivityBtn1 = (Button) findViewById(R.id.raw_ecg);
        switchActivityBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Raw_ECG.class);
                // disable default animation for new intent
                startActivity(intent1);
            }
        });


        byte[] val = new byte[10];

        try {

            InputStream is = getResources().getAssets().open("Dump9a" + ".bin");

            while (is.read(val, 0, 1) > 0) {
                inputBuffer.add(val[0]);
            }
            is.close();

        } catch (IOException e) {

        }

        //updateTextViews();
        //Check if NFC is available on device
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null) {
            //This will refer back to createNdefMessage for what it will send
            mNfcAdapter.setNdefPushMessageCallback(this, this);

            //This will be called if the message is sent successfully
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
        // inputBuffer=buildMsg.buildMsgSetTime();
        index=inputBuffer.size();
    }

    private void animatedStartActivity() {
        // we only animateOut this activity here.
        // The new activity will animateIn from its onResume() - be sure to implement it.
       Intent intent = new Intent(getApplicationContext(), patient_1.class);
        // disable default animation for new intent
        startActivity(intent);
    }


    public static int readInt(FileInputStream in) {
        Scanner scanner = new Scanner(in);
        return scanner.nextInt();
    }

    public void addText(View view) throws IOException {


        byte[] val = new byte[10];

        try {

            InputStream is = getResources().getAssets().open("Dump9a" + ".bin");
            /// BufferedReader br = new BufferedReader(new InputStreamReader(is));
/*        Scanner scan=new Scanner(is);
        while(scan.hasNextShort())
        {
            val=scan.nextShort();
            shortToSend.add(val);
        }
        scan.close();
        is.close();*/

            //updateTextViews();
            while (is.read(val, 0, 1) > 0) {
                inputBuffer.add(val[0]);
            }
            is.close();
  /*    String st = "";
        StringBuilder sb = new StringBuilder();
        while ((st=br.readLine())!=null)
        {
            sb.append(st);
        }*/
            //   br.close();

        } catch (IOException e) {

        }
        //parseInput();
        //parser.test();
        // parser.parseRateData(inputBuffer);
        //parser.parsePatientData(inputBuffer);
        //RawEcgData rawEcgDataMsg = parser.parseRawECGData(inputBuffer);
        //outMsgBuffer = buildMsg.buildMsgSetTime();
        //outMsgBuffer.clear();
        //outMsgBuffer=buildMsg.buildMsgGetData(EcgDataType.STOP_TRANSMIT);
    }


    //The array lists to hold our messages
    private ArrayList<String> messagesToSendArray = new ArrayList<>();
    private ArrayList<String> messagesReceivedArray = new ArrayList<>();

    //Text boxes to add and display our messages
    private EditText txtBoxAddMessage;
    private TextView txtReceivedMessages;
    private TextView txtMessagesToSend;

    public void addMessage(View view) {
        String newMessage = txtBoxAddMessage.getText().toString();
        messagesToSendArray.add(newMessage);

        txtBoxAddMessage.setText(null);
        //updateTextViews();

        Toast.makeText(this, "Added Message", Toast.LENGTH_LONG).show();
    }


   /* private  void updateTextViews() {
        txtMessagesToSend.setText("Messages To Send:\n");
        //Populate Our list of messages we want to send
        if(messagesToSendArray.size() > 0) {
            for (int i = 0; i < messagesToSendArray.size(); i++) {
                txtMessagesToSend.append(messagesToSendArray.get(i));
                txtMessagesToSend.append("\n");
            }
        }
        if(shortToSend.size() > 0) {
            for (int i = 0; i < shortToSend.size(); i++) {
                txtMessagesToSend.append(shortToSend.get(i).toString());
                txtMessagesToSend.append("\n");
            }
        }
       /*     if(fileBuffer.length > 0) {
                for (int i = 0; i < 20&&i<fileBuffer.length; i++) {
                    txtMessagesToSend.append(getString(fileBuffer[i]));
                    txtMessagesToSend.append("\n");
                }
            }*/
       /* txtReceivedMessages.setText("Messages Received:\n");
        //Populate our list of messages we have received
        if (messagesReceivedArray.size() > 0) {
            for (int i = 0; i < messagesReceivedArray.size(); i++) {
                txtReceivedMessages.append(messagesReceivedArray.get(i));
                txtReceivedMessages.append("\n");
            }
        }
        if (recBytes.size() > 0) {
            for (int i = 0; i < recBytes.size(); i++) {
                txtReceivedMessages.append(recBytes.get(i).toString());
                txtReceivedMessages.append("\n");
            }
        }
    }*/

    //Save our Array Lists of Messages for if the user navigates away
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("messagesToSend", messagesToSendArray);
        savedInstanceState.putStringArrayList("lastMessagesReceived",messagesReceivedArray);
    }

    //Load our Array Lists of Messages for when the user navigates back
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        messagesToSendArray = savedInstanceState.getStringArrayList("messagesToSend");
        messagesReceivedArray = savedInstanceState.getStringArrayList("lastMessagesReceived");
    }



    public NdefRecord[] createRecords() {
        short []array=new short[shortToSend.size()];
        byte[] payload=new byte[inputBuffer.size()];
        for(int i=0;i<inputBuffer.size();i++)
        {
            payload[i]=inputBuffer.get(i);
        }
        NdefRecord[] records = new NdefRecord[/*shortToSend.size()*/1 ];
        //To Create Messages Manually if API is less than
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            for (int i = 0,j=0; i < shortToSend.size(); j+=2,i++){
                array[i]=shortToSend.get(i);
                payload[j]= ((byte) (array[i] & 0xFF));
                payload[j+1]=((byte) (array[i]>>8));
            }

            NdefRecord record = new NdefRecord(
                    NdefRecord.TNF_WELL_KNOWN,      //Our 3-bit Type name format
                    NdefRecord.RTD_TEXT,            //Description of our payload
                    new byte[0],                    //The optional id for our Record
                    payload);                       //Our payload for the Record

            records[0] = record;

        }
        //Api is high enough that we can use createMime, which is preferred.
        else {
         /*   for (int i = 0,j=0; i < shortToSend.size(); j+=2,i++){
                array[i]=shortToSend.get(i);
                payload[j]= ((byte) (array[i] & 0xFF));
                payload[j+1]=((byte) (array[i]>>8));

            }*/

            NdefRecord record = NdefRecord.createMime("text/plain",payload);
            records[0] = record;

        }
        //  records[/*shortToSend.size()*/1] =
        // NdefRecord.createApplicationRecord(getPackageName());
        return records;
    }

    private void handleNfcIntent(Intent NfcIntent) {

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            byte b1,b2;
            if(receivedArray != null) {
                shortToRec.clear();
                recBytes.clear();
                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record:attachedRecords) {
                    byte[] val;
                    val=record.getPayload();
                    //Make sure we don't pass along our AAR (Android Application Record)
                    if (val.equals(getPackageName())) { continue; }

                    for (int i=0;i< val.length;i++)
                    {
                        recBytes.add(val[i]);
                    }

                }
                Toast.makeText(this, "Message Received " , Toast.LENGTH_LONG).show();
                //updateTextViews();
            }
            else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        //This is called when the system detects that our NdefMessage was
        //Successfully sent.
        shortToSend.clear();
        messagesToSendArray.clear();
        inputBuffer.clear();
        index=0;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        //This will be called when another NFC capable device is detected.
      /*  if (messagesToSendArray.size() == 0) {
            return null;
        }*/
        if (index == 0) {
            return null;
        }
        //We'll write the createRecords() method in just a moment
        NdefRecord[] recordsToAttach = createRecords();
        //When creating an NdefMessage we need to provide an NdefRecord[]
        return new NdefMessage(recordsToAttach);
    }
    public NdefRecord[] createRecords1() {

        NdefRecord[] records = new NdefRecord[messagesToSendArray.size()];

        for (int i = 0; i < messagesToSendArray.size(); i++){

            byte[] payload = messagesToSendArray.get(i).
                    getBytes(Charset.forName("UTF-8"));

            NdefRecord record = new NdefRecord(
                    NdefRecord.TNF_WELL_KNOWN,  //Our 3-bit Type name format
                    NdefRecord.RTD_TEXT,        //Description of our payload
                    new byte[0],                //The optional id for our Record
                    payload);                   //Our payload for the Record

            records[i] = record;
        }
        return records;
    }
    private void handleNfcIntent1(Intent NfcIntent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(receivedArray != null) {
                messagesReceivedArray.clear();
                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record:attachedRecords) {
                    String string = new String(record.getPayload());
                    //Make sure we don't pass along our AAR (Android Application Record)
                    if (string.equals(getPackageName())) { continue; }
                    messagesReceivedArray.add(string);
                }
                Toast.makeText(this, "Received " + messagesReceivedArray.size() +
                        " Messages", Toast.LENGTH_LONG).show();
                //updateTextViews();
            }
            else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        //updateTextViews();
        handleNfcIntent(getIntent());
    }
}

