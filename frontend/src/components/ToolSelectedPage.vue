    <template>
        <div class="selected_page">
            <div class="dragImage">
                <h3>{{ selectedPageNo }}</h3>
                <div class="object" ref="pageObject">

                </div>
            </div>
            <div class="selected_page2" v-if="content.backgroundImage" :style="{ 'background-image': `url(${require('@/assets/' + content.backgroundImage)})`, 'background-size': 'cover', 'background-repeat': 'no-repeat' }">
                <img class="character_image" v-if="content.characterImage" :src="require(`@/assets/${content.characterImage}`)">
            </div>
        </div>
    </template>
    <script>
    export default {
        props : {
            selectedPageNo : Number,
            imageList : Object,
        },
        data(){
            return{
                content: {
                    backgroundImage: null,
                    characterImage : null
                },
                pageImageList : this.imageList,
                pageImageId : '',
                selectedPageItems : [],
                currentPageNo : null,
            }
        },
        mounted() {
            this.updateContent();
        },
        watch: {
            selectedPageNo() {
                this.updateContent();
            },
        },
        methods: {
            updateContent() {    
                if (sessionStorage.getItem(this.selectedPageNo) == null) {
                    this.content.backgroundImage = null;
                    this.content.characterImage = null;
                } else {
                    this.content = JSON.parse(sessionStorage.getItem(this.selectedPageNo));
                }
                const objectElement = this.$refs.pageObject;
                //모든 자식요소 삭제
                while (objectElement.firstChild) {
                    objectElement.removeChild(objectElement.firstChild);
                }

                for (const [id, image] of Object.entries(this.imageList)) {
                    console.log(Object.entries(this.imageList));
                    if (image.pageNo === this.selectedPageNo) {
                    const imageElement = image.image;
                    objectElement.appendChild(imageElement);
                    }
                }
            },
        },
    }

</script>
<style scoped>
.selected_page{
    margin: 10px;
    width: 950px;;
    height: 450px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    touch-action: none;
    position: relative;
    border: 1px solid gray;
}
.selected_page2{
    margin: 10px;
    width: 950px;
    height: 450px;
    /* height: 60vh; */
    border: 1px solid gray;
}
.character_image{
    width: 150px;
    text-align: center;
}

.dragImage {
    margin: 10px;
    width: 950px;
    height: 450px;
    position: absolute;
}

.object {
    position: absolute;
}
</style>