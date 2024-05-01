### GNSS Spoofing Detection Android Application

#### Overview
This Android application aims to detect spoofing attacks on smartphones equipped with GNSS receivers. It utilizes statistical analysis of raw GNSS measurements to identify potential spoofing scenarios.

#### Features
1. **Spoofed Data Injection**: Authorized users can inject spoofed data from the TEXBAT or OAKBAT datasets for testing purposes.
2. **Raw GNSS Measurement Analysis**: Performs real-time analysis on raw GNSS measurements collected by the Android device.
3. **Correlation Analysis**: Utilizes the Pearson coefficient matrix to analyze correlations between C/N0 time series data of different GNSS satellites.
4. **Visual Representation**: Presents results in a heatmap format, indicating the degree of correlation between satellite signals.
5. **Threshold Classification**: Classifies spoofing scenarios based on the average Pearson coefficient exceeding a predefined threshold.

#### How to Use
1. Clone the repository to your local machine.
2. Open the Android project in Android Studio.
3. Build and run the project on your Android device.
4. Use the provided options to inject spoofed data or perform analysis on raw GNSS measurements.

#### Contributing
Contributions are welcome! Please fork the repository, make your changes, and submit a pull request.

#### Issues
If you encounter any issues or have suggestions for improvements, please open an issue on the GitHub repository.
