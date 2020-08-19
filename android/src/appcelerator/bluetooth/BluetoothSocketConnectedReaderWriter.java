/**
 * Appcelerator Titanium Mobile - Bluetooth Module
 * Copyright (c) 2020 by Axway, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package appcelerator.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

@SuppressLint("LongLogTag")
class BluetoothSocketConnectedReaderWriter
{

	private final InputStream inputStream;
	private volatile boolean isClosed = false;

	private static final String TAG = "BluetoothSocketConnectedReaderWriter";

	BluetoothSocketConnectedReaderWriter(BluetoothSocket socket) throws IOException
	{
		this.inputStream = socket.getInputStream();

		init();
	}

	private void init()
	{

		Thread readThread = new Thread(() -> {
			byte[] buffer = new byte[4096];

			while (!isClosed) {
				try {
					int read = inputStream.read(buffer);
				} catch (IOException e) {
					if (isClosed) {
						// exception while reading occurred due to closing the stream.
						return;
					}
					Log.e(TAG, "Exception while reading the inputstream.", e);
					close();
				}
			}
		});
		readThread.start();
	}

	void close()
	{
		if (isClosed) {
			Log.d(TAG, "trying to close the streams, but streams already been closed.");
			return;
		}

		isClosed = true;

		try {
			inputStream.close();
		} catch (IOException e) {
			Log.e(TAG, "exception while closing inputstream", e);
		}
	}
}
