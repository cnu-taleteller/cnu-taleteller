<template>
    <div class="book">
        <div class="book-detail">
            <div class="book-image">
                <img src="@/assets/bookDummies/book.png" class="book-image">
            </div>
            <div class="book-info">
                <h1>{{ book && book.bookName }}</h1>
                <p>{{ author }}</p>
                <p>소개: {{ book && book.bookDescription }}</p>
                <p>추천 수: {{ book && book.bookRecommend }}</p>
            </div>
            <div class="book-button">
                <button @click="recommend">추천</button>
                <button>즐겨찾기</button>
                <button @click="bookPayment(-10)">{{this.paymentCheck}}</button>
            </div>
        </div>
        <div class="book-reply">
            <reply :bookId="bookId" @created="writeReply" :replies="replies"/>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import Reply from "../components/Reply.vue";

export default {
    components: {
        Reply,
    },
    data() {
        return {
            book: null,
            bookId: this.$route.params.id,
            replies: [],
            paymentCheck:'결제',
        }
    },
    created() {
        const bookId = this.$route.params.id;

        axios
            .get(`/api/book/detail/${bookId}`)
            .then((response) => {
                this.book = response.data;
            })
            .catch((error) => {
                console.log(error);
            });
    },
    methods: {
        recommend() {
            const bookId = this.$route.params.id;

            axios
                .post(`/api/book/detail/${bookId}/recommend`)
                .then(response => {
                    this.book = response.data;
                })
                .catch(error => {
                    console.log(error);
                });
        },
        writeReply(reply) {
            this.replies.push(reply);
        },
        bookPayment(Value){
            if(confirm("결제하시겠습니까?")){
                axios.post("/api/point/bookPayment", {
                    paymentPoint : Value,
                })
                .then((res) => {
                    alert("상품이 결제되었습니다.")
                    this.paymentCheck = "감상"
                    console.log(res);
                })
                .catch((err) => {
                    console.log(err);
                });  
            }
        },

    },
};
</script>

<style scoped>
.book-detail {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    padding: 0 20%;
}

.book-image {
    flex: 1;
    margin-right: 20px;
}

.book-image img {
    width: 100%;
    max-width: 200px;
}

.book-info {
    flex: 2;
    text-align: left;
}

.book-info h1 {
    margin-bottom: 10px;
}

.book-info p {
    margin-bottom: 10px;
}

.book-button {
    display: flex;
    flex-direction: column;
}

.book-button button {
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    padding: 8px 12px;
    font-size: 16px;
    cursor: pointer;
    margin-top: 10px;
}

.book-button button:first-child {
    margin-top: 0;
}

.book-button button:hover {
    background-color: steelblue;
}

.book-reply {
    padding: 0 20%;
}
</style>