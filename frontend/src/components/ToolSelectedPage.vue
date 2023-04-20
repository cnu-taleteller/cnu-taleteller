    <template>
        <div class="selected-page">
            <h3>{{ bookIdList.pageNumber }}</h3>
            <div class="drag-image">
                <div class="object" ref="pageObject">
                    
                </div>
            </div>
        </div>
    </template>
    <script>
    export default {
        props : {
            bookIdList : Object,
            imageIndex : Number,
        },
        data(){
            return {
            }
        },
        mounted() {
            const dragArea = document.querySelector('.selected-page');
            const container = document.querySelector('.selected-page .drag-image .object');
            let toolMenu = this;
            let active = false;
            let currentX;
            let currentY;
            let currentXOffset;
            let currentYOffset;
            let initialX;
            let initialY;
            let currentObjId = null;

            container.addEventListener("mousedown", dragStart, {capture:false});
            container.addEventListener("mouseup", dragEnd, {capture:false});
            container.addEventListener("mousemove", drag, {capture:false});
            
            function dragStart(e) {
                e.stopPropagation();
                e.preventDefault();
                currentObjId = e.target.dataset.objId;
                currentX = e.pageX - dragArea.offsetLeft;
                currentY = e.pageY - dragArea.offsetTop;
                currentXOffset = e.pageX - dragArea.offsetLeft - e.offsetX;
                currentYOffset = e.pageY - dragArea.offsetTop - e.offsetY;
                console.log(currentYOffset);
                document.body.style.cursor = 'grabbing';
                e.target.style.opacity = '0.5';
                active = true;
            };

            function drag(e) {
                e.preventDefault();
                e.stopPropagation();
                if (active && currentObjId === e.target.dataset.objId) {
                    initialX = e.pageX - dragArea.offsetLeft;
                    initialY = e.pageY - dragArea.offsetTop;
                    setTranslate(initialX - currentX, initialY - currentY, e.target);
                }else if(currentObjId !== e.target.dataset.objId){
                    e.preventDefault();
                    e.stopPropagation();
                }
            };

            function setTranslate(xPos, yPos, el) {
                el.style.left = currentXOffset + xPos + "px";
                el.style.top = currentYOffset + yPos + "px";
                el.style.zIndex = "10";
            };

            function dragEnd(e) {
                e.target.style.zIndex = "1";
                e.target.style.opacity = '1';
                let targetObj = e.target.dataset.objId;
                console.log(typeof(targetObj));
                let result = toolMenu.bookIdList.imageList.find(el => el.objId === targetObj);
                result.style.left = e.target.style.left;
                result.style.top = e.target.style.top;
                document.body.style.cursor = '';
                active = false;
            };
        },
        watch: {
            bookIdList() {
                this.updateContent();
            },
        },
        methods: {
            updateContent() {
                const objectElement = this.$refs.pageObject;
                while (objectElement.firstChild) {
                    objectElement.removeChild(objectElement.firstChild);
                }
                if(this.bookIdList.imageList != null) {
                    for (const [index, image] of Object.entries(this.bookIdList.imageList)) {
                        const imageEle = document.createElement('img');
                        imageEle.src = image.src;
                        imageEle.id = image.id;
                        imageEle.dataset.objId = image.objId;
                        imageEle.style.left = image.style.left;
                        imageEle.style.top = image.style.top;
                        imageEle.style.position = image.style.position;
                        imageEle.style.width = image.style.width;
                        imageEle.style.height = image.style.height;
                        imageEle.setAttribute("draggable", "false");
                        imageEle.style.zIndex = 1;
                        objectElement.appendChild(imageEle);
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