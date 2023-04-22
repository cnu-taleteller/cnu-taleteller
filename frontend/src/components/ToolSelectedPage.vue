    <template>
        <div class="page-form">
            <div class="selected-page" >
            <div class="drag-image">
                <h3>{{ selectedPageNo }}</h3>
                <div class="object" ref="pageObject">
                </div>
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
.page-form{
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    touch-action: none;
    position: relative;
    
}
.selected-page {
    width: 100%;
    height: 60vh;
    border: 1px solid gray;
}
.character-image{
    width: 150px;
    text-align: center;
}

.drag-image {
    width: 100%;
    height: 60vh;
    position: absolute;
}

.object {
    position: absolute;
}
</style>