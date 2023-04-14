    <template>
        <div class="selected-page">
            <h3>{{ selectedPageNo }}</h3>
            <div class="drag-image">
                <div class="object" ref="pageObject">
                    
                </div>
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
                if (sessionStorage.getItem(this.selectedPageNo) != null) {
                    this.content = JSON.parse(sessionStorage.getItem(this.selectedPageNo));
                }
                const objectElement = this.$refs.pageObject;
                
                while (objectElement.firstChild) {
                    objectElement.removeChild(objectElement.firstChild);
                }

                for (const [id, image] of Object.entries(this.imageList)) {
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
.selected-page{
    margin: 10px;
    width: 80%;
    height: 450px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    touch-action: none;
    position: relative;
    border: 1px solid gray;
}
.selected-page2{
    margin: 10px;
    width: 80%;
    height: 450px;
    border: 1px solid gray;
}
.character-image{
    width: 150px;
    text-align: center;
}

.drag-image {
    margin: 10px;
    width: 100%;
    height: 450px;
    position: absolute;
}

.object {
    position: absolute;
}
</style>