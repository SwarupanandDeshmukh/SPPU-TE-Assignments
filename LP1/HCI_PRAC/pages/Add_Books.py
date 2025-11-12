

import streamlit as st;
import pandas as pd;

if "books" not in st.session_state:
    st.session_state.books = pd.DataFrame(columns=["Title","Author", "Year"]);

st.markdown(
    """
    <h2 style = 'text-align:center;' >
    ➕ Add New Books
    </h2>
    """,
    unsafe_allow_html=True
)

title = st.text_input("Book title")
author = st.text_input("Author");
year = st.number_input("Published Year", min_value = 1800, max_value = 2500, step = 1);

if st.button("Add Book", use_container_width = True):
    if title and author:
        new_book = {"Title" : title, "Author" : author, "Year" : year};
        st.session_state.books = pd.concat(
            [st.session_state.books, pd.DataFrame([new_book])],
            ignore_index = True
        )

        st.success(f"The book {title} by {author} - {year} has been added successfully");
        st.balloons();
        title = ""
        author = ""
        year = 2025
    else:
        st.error("Enter author and title")

st.info("Click on **Library Data** to see the details")



