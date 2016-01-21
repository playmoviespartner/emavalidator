/* Copyright 2014 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package emavalidator;

import com.beust.jcommander.Parameter;

/**
 * The class which represents a set of runtime parameters required when executing the validator from the command line.
 * Generated efficiently by using the JCommander external library which is absolutely fantastic. More at http://jcommander.org/
 * @author canavan
 */
public class RuntimeParameters
{
    /**
     * The fully qualified path to the input file that needs to be validated.
     * CSV vs Spreadsheet validation is decided at run time by file extension.
     * This is required, there is no default.
     */
    @Parameter(names = {"-inputfile"}, description = "The path to the input file to validate", required = true)
    private String inputFilePath = ".";

    /**
     * The name of the output file to use, if at all.
     * If fully qualified, that path is used. If not, the standard working directory where execution took place is used.
     * The default if this isn't provided is a filename generated from a generated timestamp.
     */
    @Parameter(names = {"-outputfile"}, description = "The name of the results file, if selected")
    private String outputFilePath = ".";

    /**
     * Forces the program to log the output to the console.
     * This is the default output type.
     */
    @Parameter(names = {"-logtoconsole"}, description = "Output the results to the standard console, not a file")
    private boolean loggingToConsole = true;

    /**
     * Forces the program to log the output to a file. No file name is necessarily required as a randomly generated file name will be used instead.
     * Console only output is the default.
     */
    @Parameter(names = {"-logtofile"}, description = "Output the results to a local text file")
    private boolean loggingToFile;

    /**
     * Forces the program to format the output of the program into Excel compatible CSV formatted columns
     * Log format output is the default.
     */
    @Parameter(names = {"-csvformat"}, description = "Format the output results into CSV columns")
    private boolean csvFormattedErrorLog;

    /**
     * Forces the program to format the output of the program into customized and condensed individualized errors listed in a log format.
     * This is the default output format.
     */
    @Parameter(names = {"-logformat"}, description = "Format the output results into individualized error reports in a log")
    private boolean logFormattedErrorLog = true;

    /**
     * @return The file path of the input file to validate. This field is required in order to validate.
     */
    public String getInputFilePath() { return inputFilePath; }

    /**
     * @param inputFilePath The new path of the input file to validate. This field is required in order to validate.
     */
    public void setInputFilePath(String inputFilePath) { this.inputFilePath = inputFilePath; }

    /**
     * @return The file path of the output file where the validation results should be saved. This field is not required, even if file output logging is chosen. Automatically timestamped file names are the default.
     */
    public String getOutputFilePath() { return outputFilePath; }

    /**
     * @param outputFilePath The new path of the output file where the validation results should be saved. This field is not required, even if file output logging is chosen. Automatically timestamped file names are the default.
     */
    public void setOutputFilePath(String outputFilePath) { this.outputFilePath = outputFilePath; }

    /**
     * @return Whether or not this run time instance will log its validation results to the standard console or not. This is true by default. This field is not required.
     */
    public boolean isLoggingToConsole() { return loggingToConsole; }

    /**
     * @param loggingToConsole Set whether or not this run time instance will log its validation results to the standard console or not. This is true by default. This field is not required.
     */
    public void setLoggingToConsole(boolean loggingToConsole) { this.loggingToConsole = loggingToConsole; }

    /**
     * @return Whether or not this run time instance will log to a file or not. This is false by default. This field is not required.
     */
    public boolean isLoggingToFile() { return loggingToFile; }

    /**
     * @param loggingToFile Set whether or not this run time instance will log to a file or not. This is false by default. This field is not required.
     */
    public void setLoggingToFile(boolean loggingToFile) { this.loggingToFile = loggingToFile; }

    /**
     * @return Whether or not this run time instance will log its results in CSV format or not. This is false by default. This field is not required.
     */
    public boolean isCsvFormattedErrorLog() { return csvFormattedErrorLog; }

    /**
     * @param csvFormattedErrorLog Set whether or not this run time instance will log its results in CSV format or not. This is false by default. This field is not required.
     */
    public void setCsvFormattedErrorLog(boolean csvFormattedErrorLog) { this.csvFormattedErrorLog = csvFormattedErrorLog; }

    /**
     * @return Whether or not this run time instance will log its results in Log format or not. This is true by default. This field is not required.
     */
    public boolean isLogFormattedErrorLog() { return logFormattedErrorLog; }

    /**
     * @param logFormattedErrorLog Set whether or not this run time instance will log its results in Log format or not. This is true by default. This field is not required.
     */
    public void setLogFormattedErrorLog(boolean logFormattedErrorLog) { this.logFormattedErrorLog = logFormattedErrorLog; }
}
