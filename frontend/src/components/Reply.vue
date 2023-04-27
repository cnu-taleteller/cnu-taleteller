<template>
    <div class="reply">
      <div class="reply-form">
          <form @submit.prevent="writeReply">
            <input v-model="replyContent" placeholder="댓글을 입력하세요" />
            <button type="submit">작성</button>
          </form>
      </div>
      <div class="reply-list" v-for="reply in replies" :key="reply.replyId">
          <p>내용: {{ reply.replyContent }}</p>
          <p>작성자: 테스트</p>
      </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
    props: {
        replies: {
            type: Array,
            default: () => [],
            required: true,
        },
        bookId: {
            type: Number,
            required: true,
        }
    },
    data() {
        return {
            replyContent: "",
        };
    },
    methods: {
        writeReply() {
            const payload = {
                replyContent: this.replyContent,
            };
            axios
                .post(`/api/book/detail/${this.bookId}/reply`, payload)
                .then((response) => {
                    this.$emit("created", response.data);
                    this.replyContent = "";
                })
                .catch((error) => {
                    console.log(error);
                });
        },
    },
};

</script>

<style>
.reply {
    border: 1px solid #ccc;
    padding: 20px;
    margin-bottom: 20px;
}

.reply-form {
    margin-bottom: 20px;
}

.reply-form input {
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
    font-size: 16px;
    width: 80%;
    margin-right: 10px;
}

.reply-form button {
    padding: 10px;
    border-radius: 5px;
    border: none;
    background-color: #0080ff;
    color: #fff;
    font-size: 16px;
    cursor: pointer;
}

.reply-list {
    margin-bottom: 10px;
}

.reply-list p {
    margin: 0;
    font-size: 14px;
    line-height: 1.5;
}

.reply-list p:first-child {
    font-weight: bold;
}
</style>