# Media-bias-natural-language-processing

## Overview

Media can be biased in various ways. In this work we have explored political bias of the news media as available on public websites. News articles were scraped from various left and right biased websites. Then the articles were clustered based on the similarities between them.  

## Data

The text data were scraped from the following USA based websites:

Conservative/right biased:

1. http://www.foxnews.com/politics.html
2. http://www.nationalreview.com 
3. https://www.newsmax.com
4. https://townhall.com

Liberal/left biased

1. https://www.huffingtonpost.com/section/politics
2. https://www.theatlantic.com/most-popular
3. https://washingtonmonthly.com/politics
4. https://www.nytimes.com/section/politics

## Repository structure

articles: stores all the news articles
src: contains the source codes
topics: contains the clusters of the news articles based on similarity search

## Methods

Jsoup was used to scrape the articles from the websites. Not all the websites had articles on the same news. Therefore it was important to identify and cluster news topics. Jaccard similarity test was applied to perform the clustering of news articles. Two articles from the same website were not compared. This algorithm correctly identified articles of the same news collected from different websites.

## Acknowledgments

I thank Praful Krishna and his team at Coseer for giving me this wonderful opportunity to work with them as an intern.

## Team
- [Obaidur Rahaman](https://www.linkedin.com/in/dr-obaidur-rahaman-3487879/)
- [Praful Krishna](https://www.linkedin.com/in/prafulkrishna/)
- [Mohonish Mullick](https://www.linkedin.com/in/mohonish-mullick-24756a55/)
- [Kalpesh Balar](https://www.linkedin.com/in/kalpeshbalar/)


