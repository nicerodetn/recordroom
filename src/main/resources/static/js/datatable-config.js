//function getExportButtons(moduleTitle, orientation = 'portrait') {
//
//    const headerLines = [
//        'Erode District Administration',
//        'Record Room L Section',
//        moduleTitle,
//        'Generated on: ' + new Date().toLocaleDateString()
//    ];
//
//    return [
//        // ================= PDF EXPORT =================
//        {
//            extend: 'pdfHtml5',
//            text: 'Export PDF',
//            orientation: orientation,
//            pageSize: 'A4',
//            filename: moduleTitle.replace(/\s+/g, '_'),
//
//            customize: function (doc) {
//
//                doc.pageMargins = [40, 80, 40, 60];
//                doc.defaultStyle.fontSize = 10;
//
//                // ----- HEADER -----
//                doc.content.unshift({
//                    margin: [0, 0, 0, 12],
//                    stack: [
//                        { text: headerLines[0], fontSize: 16, bold: true, alignment: 'center' },
//                        { text: headerLines[1], fontSize: 11, alignment: 'center' },
//                        { text: headerLines[2], fontSize: 13, bold: true, alignment: 'center', margin: [0, 6, 0, 0] },
//                        { text: headerLines[3], fontSize: 9, alignment: 'center', margin: [0, 4, 0, 0] }
//                    ]
//                });
//
//                // ----- TABLE HEADER STYLE -----
//                doc.styles.tableHeader = {
//                    fillColor: '#4CAF50',
//                    color: 'white',
//                    bold: true,
//                    alignment: 'center'
//                };
//
//                // ----- FOOTER -----
//                doc.footer = function (currentPage, pageCount) {
//                    return {
//                        columns: [
//                            'Erode District',
//                            {
//                                text: 'Page ' + currentPage + ' of ' + pageCount,
//                                alignment: 'right',
//                                margin: [0, 0, 20, 0]
//                            }
//                        ],
//                        margin: [40, 0]
//                    };
//                };
//            },
//
//            exportOptions: {
//                columns: ':visible',
//                format: {
//                    body: function (data, row, col, node) {
//                        return col === 0 ? $(node).text() : data;
//                    }
//                }
//            }
//        },
//
//        // ================= EXCEL EXPORT =================
//        {
//            extend: 'excelHtml5',
//            text: 'Export Excel',
//            filename: moduleTitle.replace(/\s+/g, '_'),
//            sheetName: 'Report',
//
//            customize: function (xlsx) {
//                const sheet = xlsx.xl.worksheets['sheet1.xml'];
//                const $sheet = $(sheet);
//
//                // Shift existing rows DOWN
//                const rows = $sheet.find('row');
//                rows.each(function () {
//                    const r = parseInt($(this).attr('r'));
//                    $(this).attr('r', r + 4);
//                });
//
//                rows.find('c').each(function () {
//                    const cellRef = $(this).attr('r');
//                    const col = cellRef.replace(/[0-9]/g, '');
//                    const row = parseInt(cellRef.replace(/[A-Z]/g, '')) + 4;
//                    $(this).attr('r', col + row);
//                });
//
//                function addRow(index, text, style) {
//                    return `
//                        <row r="${index}">
//                            <c t="inlineStr" r="A${index}" s="${style}">
//                                <is><t>${text}</t></is>
//                            </c>
//                        </row>`;
//                }
//
//                // Add headers
//                $sheet.prepend(addRow(4, headerLines[3], 2));
//                $sheet.prepend(addRow(3, headerLines[2], 2));
//                $sheet.prepend(addRow(2, headerLines[1], 2));
//                $sheet.prepend(addRow(1, headerLines[0], 2));
//
//                // Merge cells (A → last column)
//                const cols = $sheet.find('row[r="5"] c').length;
//                const lastCol = String.fromCharCode(64 + cols);
//
//                const mergeCells = `
//                    <mergeCells count="4">
//                        <mergeCell ref="A1:${lastCol}1"/>
//                        <mergeCell ref="A2:${lastCol}2"/>
//                        <mergeCell ref="A3:${lastCol}3"/>
//                        <mergeCell ref="A4:${lastCo
