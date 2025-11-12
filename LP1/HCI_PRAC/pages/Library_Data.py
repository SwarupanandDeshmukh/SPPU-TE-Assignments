import streamlit as st;
import pandas as pd;

if "books" not in st.session_state:
    st.session_state.books = pd.DataFrame(columns=["Title", "Author", "Year"]);


st.markdown(
    """
    <h2 style = 'text-align: center' >
    View Books
    </h2>
    """,
    unsafe_allow_html = True
)

st.markdown(
    """
    <style>
    div.stDataFrame div.row_heading,
    div.stDataFrame div.col_heading,
    div.stDataFrame {
        background-color : grey
    }
    </style>
    """,
    unsafe_allow_html = True
)


if not st.session_state.books.empty:
    st.dataframe(
        st.session_state.books,
        use_container_width = True,
        hide_index= True
    )

    st.snow()

else:
    st.info("No Books added yet");




