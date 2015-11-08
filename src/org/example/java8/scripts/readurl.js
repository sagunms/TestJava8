/**
 * READURL.JS
 */
var feed = 'https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22BHP.AX%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=';
var url = new java.net.URL(feed);
input = new java.util.Scanner(url.openStream());
input.useDelimiter('$');

var contents = input.next();
contents